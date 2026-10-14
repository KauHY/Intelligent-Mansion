/*
* Copyright (c) 2020 AIIT XUOS Lab
* XiUOS is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*        http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

/**
* @file:    abstraction_mmu.c
* @brief:   the general management of system mmu
* @version: 3.0
* @author:  AIIT XUOS Lab
* @date:    2023/4/27
*
*/

#include <abstraction_mmu.h>

AbstractionMmu abstraction_mmu;

volatile uint32_t global_L1_pte_table[4096];

/**
 * @description: write cmd to CP15 register
 * @param reg_type - CP15 register type
 * @param val - ops val pointer
 * @return 
 */
static void MmuCp15Write(uint8_t reg_type, uint32_t *val)
{
    uint32_t write_val = *val;
    switch (reg_type) {
        case AM_MMU_CP15_TTBCR:
            TTBCR_W(write_val);
            AM_ISB;
        case AM_MMU_CP15_TTBR0:
            TTBR0_W(write_val);
            AM_ISB;
        default:
            break;
    }
}

/**
 * @description: read CP15 register from mmu
 * @param reg_type - CP15 register type
 * @param val - ops val pointer
 * @return
 */
static void MmuCp15Read(uint8_t reg_type, uint32_t *val)
{
    uint32_t read_val = 0;
    switch (reg_type) {
        case AM_MMU_CP15_TTBCR:
            TTBCR_R(read_val);
        case AM_MMU_CP15_TTBR0:
            TTBR0_R(read_val);
        default:
            break;
    } 

    *val = read_val;
}

/**
 * @description: write or read CP15 register to set mmu
 * @param ops_type - CP15 write or read
 * @param reg_type - CP15 register type
 * @param val - ops val pointer
 * @return
 */
static void MmuRegOps(uint8_t ops_type, uint8_t reg_type, uint32_t *val)
{
    switch (ops_type) {
        case AM_MMU_CP15_WRITE:
            MmuCp15Write(reg_type, val);
        case AM_MMU_CP15_READ:
            MmuCp15Read(reg_type, val);
        default:
            break;
    }
}

/**
 * @description: Init abstraction_mmu function
 * @param mmu - abstraction mmu pointer
 * @param ttb_base - ttb base pointer
 * @return success : 0 error : -1
 */
static int _AbstractionMmuInit(AbstractionMmu *mmu, uint32_t *ttb_base)
{
    mmu_init();

    return 0;
}

/**
 * @description: map L1 or L2 page table section 
 * @param mmu - abstraction mmu pointer
 * @param section_size - section size
 * @return success : 0 error : -1
 */
static int _AbstractionMmuSectionMap(AbstractionMmu *mmu, uint32_t section_size)
{
    uint32_t vaddr_length = mmu->vaddr_end - mmu->vaddr_start + 1;
    
    mmu_map_l1_range(mmu->paddr_start, mmu->vaddr_start, vaddr_length, 
        mmu->mmu_memory_type, mmu->mmu_shareability, mmu->mmu_access);

    mmu->mmu_status = 1;

    return 0;
}

/**
 * @description: unmap L1 or L2 page table section 
 * @param mmu - abstraction mmu pointer
 * @param vaddr_start - virtual address start
 * @param vaddr_size - virtual address size
 * @return success : 0 error : -1
 */
static int _AbstractionMmuSectionUnmap(AbstractionMmu *mmu, uint32_t vaddr_start, uint32_t vaddr_size)
{
    uint32_t *l1_umap_ventry = mmu->ttb_vbase + (vaddr_start >> AM_MMU_L1_SECTION_SHIFT);
    uint32_t vaddr_end = vaddr_start + vaddr_size - 1;
    uint32_t umap_count = (vaddr_end >> AM_MMU_L1_SECTION_SHIFT) - (vaddr_start >> AM_MMU_L1_SECTION_SHIFT) + 1;

    while (umap_count) {
        AM_DMB;
        *l1_umap_ventry = 0;
        AM_DSB;

        umap_count--;
        l1_umap_ventry += (1 << AM_MMU_L1_SECTION_SHIFT);//1MB section
    }    

    AM_DSB;
    CLEARTLB(0);//clear TLB data and configure
    AM_DSB;
    AM_ISB;
    mmu->mmu_status = 0;

    return 0;
}   

/**
 * @description: switch ttb base by re-write ttbr register 
 * @param mmu - abstraction mmu pointer
 * @return success : 0 error : -1
 */
static int _AbstractionMmuTtbSwitch(AbstractionMmu *mmu)
{
    uint32_t ttbr, ttbcr;
    MmuRegOps(AM_MMU_CP15_READ, AM_MMU_CP15_TTBCR, &ttbcr);

    /* Set TTBR0 with inner/outer write back write allocate and not shareable, [4:3]=01, [1]=0, [6,0]=01 */
    ttbr = ((mmu->ttb_pbase & 0xFFFFC000UL) | 0x9UL);
    /* enable TTBR0 */
    ttbcr = 0;

    AM_DSB;
    MmuRegOps(AM_MMU_CP15_WRITE, AM_MMU_CP15_TTBR0, &ttbr);
    MmuRegOps(AM_MMU_CP15_WRITE, AM_MMU_CP15_TTBCR, &ttbcr);

    return 0;
}

/**
 * @description: get physical address transformed from virtual address
 * @param mmu - abstraction mmu pointer
 * @param vaddr - virtual address pointer
 * @param paddr - physical address pointer
 * @return success : 0 error : -1
 */
static int _AbstracktonMmuTransform(AbstractionMmu *mmu, uint32_t *vaddr, uint32_t *paddr)
{
    uint32_t virtualAddress = *vaddr;

    if (mmu->mmu_status) {
        mmu_virtual_to_physical(virtualAddress, paddr);
    }

    return 0;
}

static struct AbstractionMmuDone mmu_done = {
    .AbstractionMmuInit = _AbstractionMmuInit,
    .AbstractionMmuSectionMap = _AbstractionMmuSectionMap,
    .AbstractionMmuSectionUnmap = _AbstractionMmuSectionUnmap,
    .AbstractionMmuTtbSwitch = _AbstractionMmuTtbSwitch,
    .AbstracktonMmuTransform = _AbstracktonMmuTransform,
};

/**
 * @description: init abstraciton mmu info when system start
 * @return success : 0 error : -1
 */
int SysInitAbstractionMmu(void)
{
    abstraction_mmu.mmu_done = &mmu_done;
}
