/* Minimal fatal-path diagnostics for edu-riscv64's UARTHS console.
 * No printf, heap, device mutex, task-list traversal, or scheduler calls:
 * KPrintf uses a shared static buffer and simultaneous hart faults garble it.
 * These routines run only after an unrecoverable synchronous exception. */
#ifndef K210_FATAL_REPORT_H
#define K210_FATAL_REPORT_H
#if defined(BOARD_EDU_RISCV64_EVB) && defined(BSP_USING_UART_HS)
#include <stdint.h>
#include <platform.h>

static volatile unsigned int fatal_uart_lock;
static volatile unsigned int fatal_entered[2];

static void FatalPutChar(char ch)
{
    volatile uint32_t *tx = (volatile uint32_t *)(uintptr_t)UARTHS_BASE_ADDR;
    /* Bounded polling: a failed UART must not recurse or block holding a lock. */
    unsigned int spins = 1000000;
    while ((*tx & 0x80000000U) && --spins) { }
    if (spins) *tx = (unsigned char)ch;
}

static void FatalText(const char *text)
{
    while (*text) FatalPutChar(*text++);
}

static void FatalHex(uintptr_t value)
{
    static const char digits[] = "0123456789abcdef";
    FatalText("0x");
    for (int shift = (int)(sizeof(value) * 8) - 4; shift >= 0; shift -= 4)
        FatalPutChar(digits[(value >> shift) & 15]);
}

static void FatalField(const char *name, uintptr_t value)
{
    FatalText(name);
    FatalHex(value);
}

static void __attribute__((noreturn)) K210FatalReport(uintptr_t cause, uintptr_t pc, uintptr_t *frame)
{
    uintptr_t hart, value, status;
    __asm__ volatile ("csrr %0, mhartid" : "=r"(hart));
    __asm__ volatile ("csrr %0, mtval" : "=r"(value));
    __asm__ volatile ("csrr %0, mstatus" : "=r"(status));
    __asm__ volatile ("csrci mstatus, 8" ::: "memory");
    /* No frame dereference: a corrupt stack may have caused the first fault. */
    if (hart >= 2 || fatal_entered[hart]) {
        for (;;) { __asm__ volatile ("nop"); }
    }
    fatal_entered[hart] = 1;
    while (__sync_lock_test_and_set(&fatal_uart_lock, 1)) {
        __asm__ volatile ("nop");
    }
    FatalText("\r\n[FATAL v13]");
    FatalField(" hart=", hart);
    FatalField(" mcause=", cause);
    FatalField(" mepc=", pc);
    FatalField(" mtval=", value);
    FatalField(" mstatus=", status);
    FatalField(" frame=", (uintptr_t)frame);
    FatalText("\r\n[FATAL] CPU halted; reset required. Preserve this line and matching ELF.\r\n");
    __sync_lock_release(&fatal_uart_lock);
    for (;;) { __asm__ volatile ("nop"); }
}
#else
#define K210FatalReport(cause, pc, frame) ((void)0)
#endif
#endif
