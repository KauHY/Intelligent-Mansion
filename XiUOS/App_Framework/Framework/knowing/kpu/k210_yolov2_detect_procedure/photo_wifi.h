/* Dedicated ESP8285 AT transport. Only the detector task owns UART1.
 * No transparent mode: every binary block has an explicit CIPSEND length. */
#ifndef PHOTO_WIFI_H
#define PHOTO_WIFI_H
#include "photo_wifi_config.h"
#if PHOTO_WIFI_PORT < 1 || PHOTO_WIFI_PORT > 65535 || PHOTO_WIFI_ABSENT_FRAMES < 1
#error "Invalid photo Wi-Fi port or absence frame count"
#endif
#if BSP_UART1_TXD_PIN != 6 || BSP_UART1_RXD_PIN != 7
#error "Onboard ESP8285 requires UART1 TX pin 6 and RX pin 7 (docs/06)"
#endif

enum { PW_OK=1, PW_PROMPT=2, PW_SENT=3, PW_ERROR=-1 };
static int pw_fd=-1, pw_pin=-1, pw_online;
static unsigned char pw_rx[SERIAL_RB_BUFSZ];
static size_t pw_pos, pw_count, pw_line_len, pw_ack_len;
static char pw_line[192];
static unsigned char pw_ack[12];
static unsigned int pw_payload;
static uint32_t pw_transfer_start;

static void pw_reset_parser(void)
{
    pw_pos=pw_count=pw_line_len=pw_ack_len=0;
    pw_payload=0;
}

/* Handles arbitrary UART fragmentation, including binary +IPD payloads. */
static int pw_parse(unsigned char c)
{
    if (pw_payload) {
        if (pw_ack_len < sizeof(pw_ack)) pw_ack[pw_ack_len++]=c;
        else return PW_ERROR;
        --pw_payload;
        return 0;
    }
    if (c == '>') { pw_line_len=0; return PW_PROMPT; }
    if (c == '\r') return 0;
    if (c == '\n') {
        int event=0;
        pw_line[pw_line_len]=0;
        if (!strcmp(pw_line,"OK")) event=PW_OK;
        else if (!strcmp(pw_line,"SEND OK")) event=PW_SENT;
        else if (strstr(pw_line,"ERROR") || strstr(pw_line,"FAIL") ||
                 strstr(pw_line,"CLOSED") || strstr(pw_line,"WIFI DISCONNECT") ||
                 strstr(pw_line,"busy")) event=PW_ERROR;
        pw_line_len=0;
        return event;
    }
    /* A space after '>' is not part of the next response line. */
    if (!pw_line_len && c==' ') return 0;
    if (pw_line_len >= sizeof(pw_line)-1) return PW_ERROR;
    pw_line[pw_line_len++]=(char)c;
    pw_line[pw_line_len]=0;
    if (c==':' && !strncmp(pw_line,"+IPD,",5)) {
        char *end;
        unsigned long n=strtoul(pw_line+5,&end,10);
        if (end==pw_line+5 || strcmp(end,":") || !n || n>sizeof(pw_ack)-pw_ack_len)
            return PW_ERROR;
        pw_payload=(unsigned int)n;
        pw_line_len=0;
    }
    return 0;
}

static int pw_byte(void)
{
    if (pw_pos==pw_count) {
        int n=PrivRead(pw_fd,pw_rx,sizeof(pw_rx));
        if (n<=0) { PrivTaskDelay(1); return -1; }
        pw_pos=0; pw_count=(size_t)n;
    }
    return pw_rx[pw_pos++];
}

static int pw_wait(int wanted, uint32_t timeout)
{
    uint32_t start=PrivGetTickTime();
    while ((uint32_t)(PrivGetTickTime()-start)<timeout) {
        int c=pw_byte(), event;
        if (c<0) continue;
        event=pw_parse((unsigned char)c);
        if (event==PW_ERROR) return -1;
        if ((wanted && event==wanted) || (!wanted && pw_ack_len==sizeof(pw_ack) && !pw_payload)) return 0;
    }
    return -1;
}

static int pw_write(const void *data, size_t size)
{
    const unsigned char *p=data;
    while (size) {
        int n=PrivWrite(pw_fd,p,size);
        if (n<=0 || (size_t)n>size) return -1;
        p+=n; size-=(size_t)n;
    }
    return 0;
}

static int pw_command(const char *command, uint32_t timeout)
{
    if (pw_write(command,strlen(command)) || pw_write("\r\n",2)) return -1;
    return pw_wait(PW_OK,timeout);
}

static int pw_uart(unsigned int baud)
{
    struct SerialDataCfg cfg={0};
    struct PrivIoctlCfg ioctl_cfg;
    cfg.serial_baud_rate=baud;
    cfg.serial_data_bits=DATA_BITS_8; cfg.serial_stop_bits=STOP_BITS_1;
    cfg.serial_parity_mode=PARITY_NONE; cfg.serial_bit_order=BIT_ORDER_LSB;
    cfg.serial_invert_mode=NRZ_NORMAL;
    /* Leave allocated RX ring size unchanged. Timeout 0 is ignored by driver. */
    cfg.serial_timeout=1;
    ioctl_cfg.ioctl_driver_type=SERIAL_TYPE; ioctl_cfg.args=&cfg;
    return PrivIoctl(pw_fd,OPE_INT,&ioctl_cfg);
}

static int pw_quote(char *out, size_t capacity, const char *in)
{
    size_t n=0;
    for (; *in; ++in) {
        if (*in=='\r' || *in=='\n' || n+3>=capacity) return -1;
        if (*in=='"' || *in=='\\' || *in==',') out[n++]='\\';
        out[n++]=*in;
    }
    out[n]=0; return 0;
}

static int pw_connect(void)
{
    char command[320], ssid[80], password[160];
    if (!pw_online) {
        struct PinParam pin_cfg={0};
        struct PrivIoctlCfg ioctl_cfg;
        struct PinStat pin;
        uint32_t start;
        printf("[photo-wifi] initializing ESP8285\n");
        if (pw_fd<0) pw_fd=PrivOpen("/dev/uart1_dev1",O_RDWR);
        if (pw_pin<0) pw_pin=PrivOpen("/dev/pin_dev",O_RDWR);
        if (pw_fd<0 || pw_pin<0 || pw_uart(PHOTO_WIFI_BOOT_BAUD)) return -1;
        pin_cfg.cmd=GPIO_CONFIG_MODE; pin_cfg.pin=PHOTO_WIFI_ENABLE_PIN;
        pin_cfg.mode=GPIO_CFG_OUTPUT;
        ioctl_cfg.ioctl_driver_type=PIN_TYPE; ioctl_cfg.args=&pin_cfg;
        if (PrivIoctl(pw_pin,OPE_CFG,&ioctl_cfg)) return -1;
        pin.pin=PHOTO_WIFI_ENABLE_PIN; pin.val=GPIO_LOW;
        PrivWrite(pw_pin,&pin,0); PrivTaskDelay(100);
        pin.val=GPIO_HIGH; PrivWrite(pw_pin,&pin,0); PrivTaskDelay(1200);
        /* Discard reset banner (ESP boot ROM may use another UART rate). */
        pw_reset_parser(); start=PrivGetTickTime();
        while ((uint32_t)(PrivGetTickTime()-start)<300) (void)pw_byte();
        pw_reset_parser();
        if (pw_command("AT",2000) || pw_command("ATE0",2000)) return -1;
        if (PHOTO_WIFI_BAUD != PHOTO_WIFI_BOOT_BAUD) {
            snprintf(command,sizeof(command),"AT+UART_CUR=%u,8,1,0,0",(unsigned)PHOTO_WIFI_BAUD);
            if (pw_command(command,2000) || pw_uart(PHOTO_WIFI_BAUD) || pw_command("AT",2000)) return -1;
        }
        if (pw_quote(ssid,sizeof(ssid),PHOTO_WIFI_SSID) ||
            pw_quote(password,sizeof(password),PHOTO_WIFI_PASSWORD)) return -1;
        if (pw_command("AT+CWMODE=1",2000)) return -1;
        snprintf(command,sizeof(command),"AT+CWJAP=\"%s\",\"%s\"",ssid,password);
        printf("[photo-wifi] joining AP\n");
        if (pw_command(command,30000) || pw_command("AT+CIPMUX=0",2000) ||
            pw_command("AT+CIPMODE=0",2000) || pw_command("AT+CIPDINFO=0",2000)) return -1;
        pw_online=1;
    }
    pw_ack_len=0;
    snprintf(command,sizeof(command),"AT+CIPSTART=\"TCP\",\"%s\",%u",PHOTO_WIFI_PC,(unsigned)PHOTO_WIFI_PORT);
    printf("[photo-wifi] connecting PC %s:%u\n",PHOTO_WIFI_PC,(unsigned)PHOTO_WIFI_PORT);
    return pw_command(command,10000);
}

static void pw_u32(unsigned char *p, uint32_t value)
{
    p[0]=value>>24; p[1]=value>>16; p[2]=value>>8; p[3]=value;
}

static int pw_send(const unsigned char *data, size_t size)
{
    while (size) {
        char command[40];
        size_t n=size>1024 ? 1024 : size;
        if ((uint32_t)(PrivGetTickTime()-pw_transfer_start)>90000) return -1;
        snprintf(command,sizeof(command),"AT+CIPSEND=%u\r\n",(unsigned)n);
        if (pw_write(command,strlen(command)) || pw_wait(PW_PROMPT,5000) ||
            pw_write(data,n) || pw_wait(PW_SENT,5000)) return -1;
        size-=n; data+=n; PrivTaskDelay(1);
    }
    return 0;
}

static int photo_wifi_transfer(unsigned int id, const unsigned char *pixels, int width, int height)
{
    unsigned char header[24], expected[12];
    size_t size=(size_t)width*height*2;
    uint32_t crc=model_crc32(pixels,size);
    if (pw_connect()) goto fail;
    memcpy(header,"XPH1",4); pw_u32(header+4,id);
    header[8]=width>>8; header[9]=width; header[10]=height>>8; header[11]=height;
    pw_u32(header+12,(uint32_t)size); pw_u32(header+16,crc); pw_u32(header+20,1);
    memcpy(expected,"XPA1",4); pw_u32(expected+4,id); pw_u32(expected+8,crc);
    pw_transfer_start=PrivGetTickTime();
    printf("[photo-wifi] sending id=%u bytes=%u crc=%08x\n",id,(unsigned)size,(unsigned)crc);
    if (pw_send(header,sizeof(header)) || pw_send(pixels,size)) goto fail;
    if (pw_ack_len!=sizeof(pw_ack) && pw_wait(0,10000)) goto fail;
    if (pw_payload || memcmp(pw_ack,expected,sizeof(expected))) goto fail;
    printf("[photo-wifi] PC saved id=%u\n",id);
    /* PC may close after its ACK; close failure does not undo a valid save. */
    if (pw_command("AT+CIPCLOSE",2000)) pw_online=0;
    return 0;
fail:
    printf("[photo-wifi] failed: check UART1/AT firmware, Wi-Fi, PC listener and firewall; no toast\n");
    /* Reset module before next attempt, including interrupted CIPSEND state. */
    pw_online=0;
    return -1;
}
#endif
