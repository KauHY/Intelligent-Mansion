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
* @file:    mmu.h
* @brief:   the general management of system mmu
* @version: 3.0
* @author:  AIIT XUOS Lab
* @date:    2023/5/24
*
*/
#include <stdint.h>
#include <mmu.h>

#define ARCH_ARM
#ifdef ARCH_ARM
/* ARM System Registers */
#define AM_DSB     __asm__ volatile("dsb" ::: "memory")
#define AM_DMB     __asm__ volatile("dmb" ::: "memory")
#define AM_ISB     __asm__ volatile("isb" ::: "memory")
#define AM_WFI     __asm__ volatile("wfi" ::: "memory")
#define AM_BARRIER __asm__ volatile("":::"memory")
#define AM_WFE     __asm__ volatile("wfe" ::: "memory")
#define AM_SEV     __asm__ volatile("sev" ::: "memory")

#define TTBR0_R(val) __asm__ volatile("mrc p15, 0, %0, c2, c0, 0" : "=r"(val))
#define TTBR0_W(val) __asm__ volatile("mcr p15, 0, %0, c2, c0, 0" ::"r"(val))

#define TTBCR_R(val) __asm__ volatile("mrc p15, 0, %0, c2, c0, 2" : "=r"(val))
#define TTBCR_W(val) __asm__ volatile("mcr p15, 0, %0, c2, c0, 2" ::"r"(val))

#define CLEARTLB(val) __asm__ volatile("mcr p15, 0, %0, c8, c7, 0" ::"r"(val))
#endif

#define AM_MMU_L1_PAGE_TABLE_SIZE (4 * 4096)
#define AM_MMU_L1_SECTION_SHIFT 20

typedef enum 
{
    AM_MMU_CP15_WRITE = 0,
    AM_MMU_CP15_READ,
}MmuCP15OpsType;

typedef enum 
{
    AM_MMU_CP15_TTBCR = 0,
    AM_MMU_CP15_TTBR0,
    AM_MMU_CP15_CLEARTLB,
}MmuCP15RegType;

typedef enum 
{
    AM_StronglyOrdered = 0,
    AM_Device,
    AM_OuterInner_WB_WA,
    AM_OuterInner_WT,
    AM_Noncacheable,
}MmuMemoryType;

typedef enum 
{
    AM_Noaccess = 0,
    AM_Read_Write,
    AM_Read,
}MmuAccess;

typedef enum
{
    AM_Shareable = 1,
    AM_Nonshareable = 0
}MmuShareability;

struct AbstractionMmuDone
{
    int (*AbstractionMmuInit)(AbstractionMmu *mmu, uint32_t *ttb_base);
    int (*AbstractionMmuSectionMap)(AbstractionMmu *mmu, uint32_t section_size);
    int (*AbstractionMmuSectionUnmap)(AbstractionMmu *mmu, uint32_t vaddr_start, uint32_t vaddr_size);
    int (*AbstractionMmuTtbSwitch)(AbstractionMmu *mmu);
    int (*AbstracktonMmuTransform)(AbstractionMmu *mmu, uint32_t *vaddr, uint32_t *paddr);
};

typedef struct
{
    uint32_t ttb_vbase;
    uint32_t ttb_pbase;

    uint32_t vaddr_start;
    uint32_t vaddr_end;
    uint32_t paddr_start;
    uint32_t paddr_end;

    uint32_t vpaddr_offset;

    uint32_t pte_attr;
    uint32_t mmu_status;

    MmuMemoryType mmu_memory_type;
    MmuAccess mmu_access;
    MmuShareability mmu_shareability;

    struct AbstractionMmuDone *mmu_done;

    int lock;
    int link_list;
}AbstractionMmu;