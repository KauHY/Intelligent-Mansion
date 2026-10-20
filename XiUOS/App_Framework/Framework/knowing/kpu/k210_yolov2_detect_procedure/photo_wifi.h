/* Dedicated ESP8285 AT transport. Only the detector task owns UART1.
 * No transparent mode: every binary block has an explicit CIPSEND length. */
#ifndef PHOTO_WIFI_H
#define PHOTO_WIFI_H
#include "photo_wifi_config.h"
#if PHOTO_WIFI_PORT < 1 || PHOTO_WIFI_PORT > 65535 || PHOTO_WIFI_ABSENT_FRAMES < 1
#error "Invalid photo Wi-Fi port or absence frame count"
#endif
/* WIFI_TX/WIFI_RX name the ESP signals: cross them at the K210.
 * Same-board reference: Nuttx_Fusion_XiUOS/aiit_board/edu-riscv64/src/k210_bringup.c. */
#if BSP_UART1_TXD_PIN != 7 || BSP_UART1_RXD_PIN != 6
#error "Onboard ESP8285 requires K210 UART1 TX=7 (ESP RX), RX=6 (ESP TX)"
#endif

enum { PW_OK=1, PW_PROMPT=2, PW_SENT=3, PW_ERROR=-1 };
static int pw_fd=-1, pw_pin=-1, pw_online;
static unsigned char pw_rx[SERIAL_RB_BUFSZ];
static size_t pw_pos, pw_count, pw_line_len, pw_ack_len;
static char pw_line[192];
static unsigned char pw_ack[12];
static unsigned int pw_payload;
static uint32_t pw_transfer_start;
static const char *pw_stage="idle";
static unsigned int pw_rx_bytes, pw_actual_baud;
static int pw_closing;
static int pw_joining, pw_join_code;
static unsigned int pw_wire_offset;
static int pw_block_received=-1;
static int pw_terminal_failure;
#ifndef PHOTO_WIFI_BLOCK_BYTES
#define PHOTO_WIFI_BLOCK_BYTES 1024
#endif
#ifndef PHOTO_WIFI_FRAME_TIMEOUT_MS
#define PHOTO_WIFI_FRAME_TIMEOUT_MS 300000U
#endif
#if PHOTO_WIFI_BLOCK_BYTES < 64 || PHOTO_WIFI_BLOCK_BYTES > 2048
#error "PHOTO_WIFI_BLOCK_BYTES must be 64..2048"
#endif

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
        /* Only log known status tokens; command echo may contain credentials. */
        if (!strcmp(pw_line,"WIFI DISCONNECT")) {
            printf("[photo-wifi] WIFI DISCONNECT%s\n",pw_joining ? " (joining: wait for final result)" : "");
            if (!pw_joining) event=PW_ERROR;
        } else if (!strcmp(pw_line,"WIFI CONNECTED") || !strcmp(pw_line,"WIFI GOT IP")) {
            printf("[photo-wifi] %s\n",pw_line);
        } else if (pw_joining && (!strncmp(pw_line,"+CWJAP:",7) || !strncmp(pw_line,"+CWJAP_CUR:",11))) {
            char *end;
            long code=strtol(strchr(pw_line,':')+1,&end,10);
            if (!*end && code>0 && code<256) {
                pw_join_code=(int)code;
                printf("[photo-wifi] CWJAP error=%d (%s)\n",pw_join_code,
                       code==1 ? "connection timeout" : code==2 ? "wrong password" :
                       code==3 ? "AP not found" : code==4 ? "connection failed" : "unknown");
            }
        }
        if (!strncmp(pw_line,"Recv ",5)) {
            unsigned int count; char tail;
            if(sscanf(pw_line,"Recv %u bytes%c",&count,&tail)==1 && count<=2048)
                pw_block_received=(int)count;
        }
        if (!strcmp(pw_line,"OK")) event=PW_OK;
        else if (!strcmp(pw_line,"SEND OK")) event=PW_SENT;
        else if (!strcmp(pw_line,"ERROR") || !strcmp(pw_line,"FAIL") || !strcmp(pw_line,"SEND FAIL") ||
                 (!pw_closing && !strcmp(pw_line,"CLOSED")) || !strncmp(pw_line,"busy",4)) {
            printf("[photo-wifi] AT final failure (%s)\n",
                   !strncmp(pw_line,"busy",4) ? "busy" : pw_line);
            event=PW_ERROR;
            if (!strcmp(pw_line,"ERROR") || !strcmp(pw_line,"FAIL"))
                pw_terminal_failure=1;
        }
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
        pw_rx_bytes+=(unsigned)n;
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
        if (event==PW_ERROR) { printf("[photo-wifi] stage=%s response/protocol error; rx=%u\n",pw_stage,pw_rx_bytes); return -1; }
        if ((wanted && event==wanted) || (!wanted && pw_ack_len==sizeof(pw_ack) && !pw_payload)) return 0;
    }
    printf("[photo-wifi] stage=%s timeout waiting=%d rx=%u baud=%u\n",pw_stage,wanted,pw_rx_bytes,pw_actual_baud);
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
    int result;
    pw_terminal_failure=0;
    pw_joining=!strncmp(command,"AT+CWJAP=",9);
    if (pw_joining) pw_join_code=0;
    /* Never log the CWJAP command: it contains credentials. */
    if (!strncmp(command,"AT+CWJAP",8)) pw_stage="join AP";
    else if (!strncmp(command,"AT+CIPSTART",11)) pw_stage="TCP connect";
    else if (!strncmp(command,"AT+UART",7)) pw_stage="UART change";
    else if (!strcmp(command,"AT")) pw_stage="AT handshake";
    else if (!strcmp(command,"ATE0")) pw_stage="disable echo";
    else if (!strcmp(command,"AT+CWMODE=1")) pw_stage="station mode";
    else if (!strcmp(command,"AT+CIPMUX=0")) pw_stage="single connection";
    else if (!strcmp(command,"AT+CIPMODE=0")) pw_stage="normal send mode";
    else if (!strcmp(command,"AT+CIPDINFO=0")) pw_stage="IPD format";
    else pw_stage="close socket";
    if (pw_write(command,strlen(command)) || pw_write("\r\n",2)) {
        pw_joining=0;
        return -1;
    }
    result=pw_wait(PW_OK,timeout);
    if (pw_joining && pw_join_code) result=-1;
    pw_joining=0;
    return result;
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
    pw_stage="UART configuration";
    pw_actual_baud=baud;
    return PrivIoctl(pw_fd,OPE_INT,&ioctl_cfg);
}

/* Boot banners and auto-reconnect notifications must not poison a new AT line. */
static void pw_drain(uint32_t duration)
{
    uint32_t start=PrivGetTickTime();
    pw_reset_parser();
    while ((uint32_t)(PrivGetTickTime()-start)<duration) (void)pw_byte();
    pw_reset_parser();
    pw_rx_bytes=0;
}

static int pw_probe(void)
{
    const unsigned rates[]={PHOTO_WIFI_BOOT_BAUD,PHOTO_WIFI_BAUD,115200,921600,460800,57600,38400,19200,9600};
    for (size_t i=0;i<sizeof(rates)/sizeof(rates[0]);i++) {
        size_t j;
        for(j=0;j<i;j++) if(rates[j]==rates[i]) break;
        if(j<i) continue;
        if(pw_uart(rates[i])) return -1;
        printf("[photo-wifi] probe UART1 baud=%u TX=7 RX=6\n",rates[i]);
        for(int attempt=0;attempt<2;attempt++) {
            pw_drain(200);
            if(!pw_command("AT",1200)) {
                printf("[photo-wifi] AT ready baud=%u\n",rates[i]);
                return 0;
            }
        }
    }
    return -1;
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

        printf("[photo-wifi] initializing ESP8285\n");
        pw_stage="open UART1";
        if (pw_fd<0) pw_fd=PrivOpen("/dev/uart1_dev1",O_RDWR);
        if(pw_fd<0) return -1;
        pw_stage="open GPIO";
        if (pw_pin<0) pw_pin=PrivOpen("/dev/pin_dev",O_RDWR);
        if (pw_fd<0 || pw_pin<0 || pw_uart(PHOTO_WIFI_BOOT_BAUD)) return -1;
        pin_cfg.cmd=GPIO_CONFIG_MODE; pin_cfg.pin=PHOTO_WIFI_ENABLE_PIN;
        pin_cfg.mode=GPIO_CFG_OUTPUT;
        ioctl_cfg.ioctl_driver_type=PIN_TYPE; ioctl_cfg.args=&pin_cfg;
        pw_stage="configure EN GPIO";
        if (PrivIoctl(pw_pin,OPE_CFG,&ioctl_cfg)) return -1;
        pin.pin=PHOTO_WIFI_ENABLE_PIN; pin.val=GPIO_LOW;
        PrivWrite(pw_pin,&pin,0); PrivTaskDelay(100);
        pin.val=GPIO_HIGH; PrivWrite(pw_pin,&pin,0); PrivTaskDelay(2500);
        if (pw_probe() || pw_command("ATE0",2000)) return -1;
        if (PHOTO_WIFI_BAUD != pw_actual_baud) {
            snprintf(command,sizeof(command),"AT+UART_CUR=%u,8,1,0,0",(unsigned)PHOTO_WIFI_BAUD);
            /* Firmware versions differ in which baud carries the OK response.
             * Verify at the new rate instead of requiring OK at the old rate. */
            pw_stage="UART change";
            if(pw_write(command,strlen(command)) || pw_write("\r\n",2)) return -1;
            PrivTaskDelay(200);
            if(pw_uart(PHOTO_WIFI_BAUD)) return -1;
            pw_drain(200);
            if(pw_command("AT",2000)) return -1;
        }
        if (pw_quote(ssid,sizeof(ssid),PHOTO_WIFI_SSID) ||
            pw_quote(password,sizeof(password),PHOTO_WIFI_PASSWORD)) return -1;
        if (pw_command("AT+CWMODE=1",2000)) return -1;
        snprintf(command,sizeof(command),"AT+CWJAP=\"%s\",\"%s\"",ssid,password);
        printf("[photo-wifi] joining AP\n");
        /* Retry only after a complete failure response. A host-side timeout
         * may leave an AT command active: do not append another command then. */
        int joined=0;
        for (int attempt=0;attempt<3;attempt++) {
            if (!pw_command(command,45000)) { joined=1; break; }
            if (!pw_terminal_failure || pw_join_code==2 ||
                (pw_join_code!=1 && pw_join_code!=3 && pw_join_code!=4)) break;
            if (attempt<2) {
                printf("[photo-wifi] join retry=%d/3 after terminal failure\n",attempt+2);
                PrivTaskDelay(1000*(attempt+1));
            }
        }
        if (!joined || pw_command("AT+CIPMUX=0",2000) ||
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
        size_t n=size>PHOTO_WIFI_BLOCK_BYTES ? PHOTO_WIFI_BLOCK_BYTES : size;
        uint32_t start=PrivGetTickTime();
        unsigned int rx_before=pw_rx_bytes;
        if ((uint32_t)(start-pw_transfer_start)>=PHOTO_WIFI_FRAME_TIMEOUT_MS) {
            pw_stage="frame deadline";
            printf("[photo-wifi] frame deadline elapsed_ms=%u limit_ms=%u confirmed_wire=%u\n",
                   (unsigned)(start-pw_transfer_start),
                   (unsigned)PHOTO_WIFI_FRAME_TIMEOUT_MS,pw_wire_offset);
            return -1;
        }
        pw_block_received=-1;
        snprintf(command,sizeof(command),"AT+CIPSEND=%u\r\n",(unsigned)n);
        pw_stage="CIPSEND prompt";
        if (pw_write(command,strlen(command)) || pw_wait(PW_PROMPT,5000)) goto block_fail;
        pw_stage="UART binary write";
        /* Small writes and gaps let the module consume its UART FIFO without RTS/CTS. */
        for(size_t offset=0;offset<n;) {
            size_t count=n-offset>64 ? 64 : n-offset;
            if(pw_write(data+offset,count)) goto block_fail;
            offset+=count;
            PrivTaskDelay(2);
        }
        pw_stage="SEND OK";
        if(pw_wait(PW_SENT,15000)) goto block_fail;
        pw_wire_offset+=(unsigned)n;
        if((pw_wire_offset-24)%8192==0 || size==n)
            printf("[photo-wifi] confirmed wire=%u elapsed_ms=%u\n",pw_wire_offset,
                   (unsigned)(PrivGetTickTime()-pw_transfer_start));
        size-=n; data+=n; PrivTaskDelay(2);
        continue;
block_fail:
        printf("[photo-wifi] block offset=%u length=%u module_received=%d rx_delta=%u elapsed_ms=%u frame_elapsed_ms=%u; not resent\n",
               pw_wire_offset,(unsigned)n,pw_block_received,pw_rx_bytes-rx_before,
               (unsigned)(PrivGetTickTime()-start),
               (unsigned)(PrivGetTickTime()-pw_transfer_start));
        return -1;
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
    pw_wire_offset=0;
    printf("[photo-wifi v8] block=%u UART burst=64 gap=2ms SEND_OK timeout=15s frame_limit_ms=%u\n",
           (unsigned)PHOTO_WIFI_BLOCK_BYTES,(unsigned)PHOTO_WIFI_FRAME_TIMEOUT_MS);
    printf("[photo-wifi] sending id=%u bytes=%u crc=%08x\n",id,(unsigned)size,(unsigned)crc);
    if (pw_send(header,sizeof(header)) || pw_send(pixels,size)) goto fail;
    pw_stage="PC saved ACK";
    if (pw_ack_len!=sizeof(pw_ack) && pw_wait(0,10000)) goto fail;
    if (pw_payload || memcmp(pw_ack,expected,sizeof(expected))) goto fail;
    printf("[photo-wifi] PC saved id=%u transfer_ms=%u bytes=%u baud=%u\n",id,
           (unsigned)(PrivGetTickTime()-pw_transfer_start),(unsigned)size,pw_actual_baud);
    /* PC may close after its ACK; close failure does not undo a valid save. */
    pw_closing=1;
    if (pw_command("AT+CIPCLOSE",2000)) pw_online=0;
    pw_closing=0;
    return 0;
fail:
    printf("[photo-wifi] failed stage=%s baud=%u rx=%u; no toast\n",pw_stage,pw_actual_baud,pw_rx_bytes);
    /* Reset module before next attempt, including interrupted CIPSEND state. */
    pw_online=0;
    return -1;
}
#endif
