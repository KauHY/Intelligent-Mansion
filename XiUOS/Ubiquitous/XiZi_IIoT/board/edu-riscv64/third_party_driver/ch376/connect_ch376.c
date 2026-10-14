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
* @file connect_ch376.c
* @brief support to register ch376 pointer and function
* @version 2.0 
* @author AIIT XUOS Lab
* @date 2022-10-17
*/

#include <ch376fs.h>
#include <connect_ch376.h>
#include <clint.h>
#include <sysctl.h>

static struct HwCh376 ch376;
static unsigned int ch376_last_cmd;
static unsigned int ch376_trace_count;
static int ch376_rx_fault;
/* Drain the UART FIFO in one read, then serve exact protocol fields.
 * This also preserves coalesced header/payload bytes across bus reads. */
static uint8 ch376_rx_cache[512];
static x_size_t ch376_rx_pos;
static x_size_t ch376_rx_count;
#define CH376_RX_TIMEOUT_SECONDS 5U
#define CH376_TRACE_LIMIT 24U

static uint32 Ch376UartWrite(struct BusBlockWriteParam *tx)
{
    if (ch376_rx_fault || !tx || (!tx->buffer && tx->size))
        return (uint32)-ERROR;
    /* UART polling write returns a byte count; the CH376 bus returns EOK. */
    uint32 sent = BusDevWriteData(ch376.dev, tx);
    if (sent == tx->size) return EOK;
    ch376_rx_fault = 1;
    KPrintf("[CH376] TX ERROR cmd=0x%02x expected=%u returned=%u; reset board\n",
            ch376_last_cmd, (unsigned int)tx->size, (unsigned int)sent);
    return (uint32)-ERROR;
}

static uint32 Ch376Configure(void *drv, struct BusConfigureInfo *configure_info)
{
    NULL_PARAM_CHECK(drv);
    NULL_PARAM_CHECK(configure_info);
    if (ch376_rx_fault) return (uint32)-ERROR;
    ch376_last_cmd = (unsigned int)configure_info->configure_cmd;
    uint8 command = (uint8)configure_info->configure_cmd;
    uint8 prefix[2] = {0x57, 0xab};
    struct BusBlockWriteParam tx = {0};
    /* Retain the board's existing command spacing for this correctness fix. */
    MdelayKTask(100);
    tx.buffer = prefix;
    tx.size = sizeof(prefix);
    if (Ch376UartWrite(&tx) != EOK) return (uint32)-ERROR;
    tx.buffer = &command;
    tx.size = 1;
    if (Ch376UartWrite(&tx) != EOK) return (uint32)-ERROR;
    return EOK;
}

static int HwCh376RxInd(void *dev, x_size_t length)
{
    /* length is a FIFO occupancy snapshot, NOT a newly received byte count.
     * The UART already signals its own dev_sem. Do not duplicate signals. */
    ch376.msg_len = length;
    return EOK;
}

static uint32 Ch376Open(void *dev)
{
    NULL_PARAM_CHECK(dev);
    struct Bus *bus = BusFind(SERIAL_BUS_NAME_3);
    if (!bus) return (uint32)-ERROR;
    struct Driver *bus_driver = BusFindDriver(bus, SERIAL_DRV_NAME_3);
    ch376.dev = BusFindDevice(bus, SERIAL_3_DEVICE_NAME_0);
    if (!bus_driver || !ch376.dev) return (uint32)-ERROR;

    struct SerialCfgParam serial_cfg = {0};
    struct BusConfigureInfo cfg = {0};
    serial_cfg.data_cfg.serial_buffer_size = sizeof(ch376_rx_cache);
    cfg.configure_cmd = OPE_INT;
    cfg.private_data = &serial_cfg;
    if (BusDrvConfigure(bus_driver, &cfg) != EOK) return (uint32)-ERROR;
    bus->match(bus_driver, ch376.dev);

    struct SerialDevParam *param = (struct SerialDevParam *)ch376.dev->private_data;
    param->serial_set_mode = SIGN_OPER_INT_RX;
    /* Nonblocking UART reads; the outer receive loop owns the deadline. */
    param->serial_timeout = 0;
    ch376_rx_pos = ch376_rx_count = 0;
    ch376.msg_len = 0;
    ch376_rx_fault = 0;
    ch376_trace_count = 0;
    if (BusDevOpen(ch376.dev) != EOK) return (uint32)-ERROR;
    BusDevRecvCallback(ch376.dev, HwCh376RxInd);
    KPrintf("CH376 open done\n");
    KPrintf("[CH376] RX FIX v5: shared CLINT clock, timeout=5s\n");
    return EOK;
}

static uint32 Ch376Close(void *dev)
{
    BusDevRecvCallback(ch376.dev, NONE);
    ch376_rx_fault = 1;
    ch376_rx_pos = ch376_rx_count = 0;
    return BusDevClose(ch376.dev);
}

static uint32 Ch376Write(void *dev, struct BusBlockWriteParam *write_param)
{
    return Ch376UartWrite(write_param);
}

static uint32 Ch376Read(void *dev, struct BusBlockReadParam *read_param)
{
    if (!read_param) return (uint32)-ERROR;
    read_param->read_length = 0;
    if (ch376_rx_fault || !read_param->buffer || read_param->size > PARAM_LEN)
        return (uint32)-ERROR;
    x_size_t wanted = read_param->size;
    /* CurrentTicksGain is per-CPU under ARCH_SMP. Never subtract readings
     * across a sleep/migration. CLINT mtime is shared by both K210 cores. */
    uint64_t clock_hz = SysctlClockGetFreq(SYSCTL_CLOCK_CPU) / CLINT_CLOCK_DIV;
    if (!clock_hz) {
        KPrintf("[CH376] invalid CLINT frequency\n");
        ch376_rx_fault = 1;
        return (uint32)-ERROR;
    }
    uint64_t start = clint_get_time();
    uint64_t timeout_cycles = clock_hz * CH376_RX_TIMEOUT_SECONDS;
    uint8 *dst = (uint8 *)read_param->buffer;
    unsigned int empty_polls = 0;
    unsigned int zero_reads = 0;

    while (read_param->read_length < wanted) {
        if (ch376_rx_pos < ch376_rx_count) {
            x_size_t n = ch376_rx_count - ch376_rx_pos;
            x_size_t remaining = wanted - read_param->read_length;
            if (n > remaining) n = remaining;
            memcpy(dst + read_param->read_length, ch376_rx_cache + ch376_rx_pos, n);
            ch376_rx_pos += n;
            read_param->read_length += n;
            continue;
        }
        uint64_t elapsed = clint_get_time() - start;
        if (elapsed >= timeout_cycles) {
            KPrintf("[CH376] RX TIMEOUT cmd=0x%02x got=%u want=%u empty_polls=%u zero_reads=%u elapsed_ms=%u; reset board\n",
                    ch376_last_cmd, (unsigned int)read_param->read_length, (unsigned int)wanted,
                    empty_polls, zero_reads, (unsigned int)(elapsed * 1000ULL / clock_hz));
            goto failed;
        }
        struct BusBlockReadParam uart_read = {0};
        uart_read.buffer = ch376_rx_cache;
        uart_read.size = sizeof(ch376_rx_cache);
        /* The serial driver increments read_length, so use a fresh zeroed
         * descriptor for EVERY call, including empty FIFO polls. */
        uint32 ret = BusDevReadData(ch376.dev, &uart_read);
        /* SerialDevRead returns ERROR when its zero-timeout semaphore has
         * no token. This is not a fatal receive error. Its API also uses
         * ERROR for other failures: persistent empty failures therefore
         * expire at the outer deadline, never masquerade as valid data. */
        if (ret == ERROR && uart_read.read_length == 0) {
            if (empty_polls++ == 0 && ch376_trace_count < CH376_TRACE_LIMIT)
                KPrintf("[CH376] RX WAIT cmd=0x%02x ret=%u; waiting up to 5s\n",
                        ch376_last_cmd, (unsigned int)ret);
            MdelayKTask(1);
            continue;
        }
        if (ret != EOK ||
            uart_read.read_length > sizeof(ch376_rx_cache)) {
            KPrintf("[CH376] UART READ ERROR cmd=0x%02x ret=%u bytes=%u\n",
                    ch376_last_cmd, (unsigned int)ret, (unsigned int)uart_read.read_length);
            goto failed;
        }
        ch376_rx_pos = 0;
        ch376_rx_count = uart_read.read_length;
        if (ch376_rx_count == 0) {
            zero_reads++;
            MdelayKTask(1);
        }
    }
    if (ch376_trace_count++ < CH376_TRACE_LIMIT)
        KPrintf("[CH376] rx cmd=0x%02x got=%u want=%u first=0x%02x\n", ch376_last_cmd,
                (unsigned int)read_param->read_length, (unsigned int)wanted,
                wanted ? (unsigned int)dst[0] : 0U);
    return (uint32)read_param->read_length;
failed:
    /* Never reuse late bytes as the next command's status after a timeout. */
    ch376_rx_fault = 1;
    ch376_rx_pos = ch376_rx_count = 0;
    return (uint32)-ERROR;
}

#ifdef BSP_USING_SDIO
static struct SdioDevDone sdio_done = 
{
    Ch376Open,
    Ch376Close,
    Ch376Write,
    Ch376Read,
};
#endif

#ifdef BSP_USING_USB
static struct UsbDevDone usb_done = 
{
    Ch376Open,
    Ch376Close,
    Ch376Write,
    Ch376Read,
};
#endif

int HwCh376Init(void)
{
    x_err_t ret = EOK;

#ifdef BSP_USING_SDIO
    static struct SdioBus sdio_bus;
    static struct SdioDriver sdio_drv;
    static struct SdioHardwareDevice sdio_dev;

    ret = SdioBusInit(&sdio_bus,SDIO_BUS_NAME );
    if (ret != EOK) {
        KPrintf("Sdio bus init error %d\n", ret);
        return -ERROR;
    }

    sdio_drv.configure = &Ch376Configure;
    ret = SdioDriverInit(&sdio_drv, SDIO_DRIVER_NAME);
    if (ret != EOK) {
        KPrintf("Sdio driver init error %d\n", ret);
        return -ERROR;
    }

    ret = SdioDriverAttachToBus( SDIO_DRIVER_NAME, SDIO_BUS_NAME);
    if (ret != EOK) {
        KPrintf("Sdio driver attach error %d\n", ret);
        return -ERROR;
    }

    sdio_dev.dev_done = &sdio_done;
    ret = SdioDeviceRegister(&sdio_dev, SDIO_DEVICE_NAME);
    if (ret != EOK) {
        KPrintf("Sdio device register error %d\n", ret);
        return -ERROR;
    }

    ret = SdioDeviceAttachToBus(SDIO_DEVICE_NAME, SDIO_BUS_NAME);
    if (ret != EOK) {
        KPrintf("Sdio device register error %d\n", ret);
        return -ERROR;
    }
#endif

#ifdef BSP_USING_USB
    static struct UsbBus usb_bus;
    static struct UsbDriver usb_drv;
    static struct UsbHardwareDevice usb_dev;
    
    ret = UsbBusInit(&usb_bus, USB_BUS_NAME);
    if (ret != EOK) {
        KPrintf("USB bus init error %d\n", ret);
        return -ERROR;
    }

    usb_drv.configure = &Ch376Configure;
    ret = UsbDriverInit(&usb_drv, USB_DRIVER_NAME);
    if (ret != EOK) {
        KPrintf("USB driver init error %d\n", ret);
        return -ERROR;
    }
    ret = UsbDriverAttachToBus(USB_DRIVER_NAME, USB_BUS_NAME);
    if (ret != EOK) {
        KPrintf("USB driver attach error %d\n", ret);
        return -ERROR;
    }

    usb_dev.dev_done = &usb_done;
    ret = USBDeviceRegister(&usb_dev, NONE, USB_DEVICE_NAME);
    if (ret != EOK) {
        KPrintf("USB device register error %d\n", ret);
        return -ERROR;
    }
    ret = USBDeviceAttachToBus(USB_DEVICE_NAME, USB_BUS_NAME);
    if (ret != EOK) {
        KPrintf("USB device register error %d\n", ret);
        return -ERROR;
    }
#endif
    return ret;
}
