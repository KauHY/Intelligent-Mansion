/* Host integration harness: actual photo_bridge.h + photo_wifi.h, fake AT peer. */
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#define ADD_XIZI_FEATURES 1
#define BSP_USING_LCD 1
#define PHOTO_WIFI_TCP 1
#define BSP_UART1_TXD_PIN 7
#define BSP_UART1_RXD_PIN 6
#define LCD_SIZE 320
#define LCD_DOT_TYPE 1
#define NULL_PARAMETER 0
#define SERIAL_RB_BUFSZ 128
#define DATA_BITS_8 8
#define STOP_BITS_1 1
#define PARITY_NONE 0
#define BIT_ORDER_LSB 0
#define NRZ_NORMAL 0
#define SERIAL_TYPE 1
#define PIN_TYPE 2
#define OPE_INT 1
#define OPE_CFG 2
#define O_RDWR 2
#define GPIO_CONFIG_MODE 1
#define GPIO_CFG_OUTPUT 1
#define GPIO_LOW 0
#define GPIO_HIGH 1
typedef int pthread_mutex_t;
typedef struct { int type; struct {uint16_t *pixel_color; int x_startpos,x_endpos,y_startpos,y_endpos;} pixel_info;} LcdWriteParam;
struct SerialDataCfg {unsigned serial_baud_rate; int serial_data_bits,serial_stop_bits,serial_parity_mode,serial_bit_order,serial_invert_mode,serial_timeout;};
struct PrivIoctlCfg {int ioctl_driver_type; void *args;};
struct PinParam {int cmd,pin,mode;};
struct PinStat {long pin; unsigned short val;};
static uint32_t ticks;
static int locked, lcd_writes, toast, fault, pending;
#define IMAGE_BYTES (320*240*2)
static unsigned char queue[4096], wire[IMAGE_BYTES+24];
static size_t qr,qw,wire_len,cmd_len;
static char cmd[512];
static unsigned host_baud=115200, module_baud=115200;
static int skip_at;
static int join_failure;
static int transient_joins, join_calls, send_calls;
static uint32_t delayed_ok;
static int suppress_send_ok;
static int close_payload;
static int block_requested;
static void enqueue(const void *p,size_t n) {assert(qw+n<=sizeof(queue));memcpy(queue+qw,p,n);qw+=n;}
static void line(const char *p) {enqueue(p,strlen(p));}
static uint32_t PrivGetTickTime(void) {return ticks++;}
static void PrivTaskDelay(int ms) {assert(!locked);ticks+=ms;if(ms==2000 && lcd_writes%80==40)toast++;}
static int PrivMutexCreate(int *m,void *p) {(void)m;(void)p;return 0;}
static int PrivMutexObtain(int *m) {(void)m;assert(!locked);locked=1;return 0;}
static int PrivMutexAbandon(int *m) {(void)m;assert(locked);locked=0;return 0;}
static int PrivOpen(const char *p,int mode) {(void)mode;return strstr(p,"uart") ? 1:2;}
static int PrivIoctl(int fd,int mode,struct PrivIoctlCfg *cfg) {
    (void)fd;(void)mode;
    if(cfg->ioctl_driver_type==SERIAL_TYPE)host_baud=((struct SerialDataCfg *)cfg->args)->serial_baud_rate;
    return 0;
}
static int PrivRead(int fd,void *p,size_t n) {
    if(delayed_ok && ticks>=delayed_ok) {line("\r\nSEND OK\r\n");delayed_ok=0;}
    size_t count=qw-qr; (void)fd;
    if(count>7)count=7; /* fragment AT lines, +IPD headers and binary ACK */
    if(count>n)count=n;
    memcpy(p,queue+qr,count);qr+=count;
    if(qr==qw)qr=qw=0;
    return (int)count;
}
static int PrivWrite(int fd,const void *p,size_t n) {
    const unsigned char *bytes=p;
    if(fd==2)return 0;
    if(fd==3){const LcdWriteParam *v=p;assert(v->pixel_info.y_startpos>=280 && v->pixel_info.y_endpos<320);lcd_writes++;return 0;}
    if(n>17)n=17; /* short writes must not lose bytes */
    for(size_t i=0;i<n;i++) {
        if(pending) {
            wire[wire_len++]=bytes[i];
            if(!--pending) {
                if(close_payload && wire_len>24) {
                    char response[80];
                    snprintf(response,sizeof(response),"\r\nRecv %u bytes\r\nCLOSED\r\n",
                             (unsigned)block_requested);
                    line(response);
                } else {
                if(wire_len==sizeof(wire) && fault!=3) {
                    unsigned char ack[12];memcpy(ack,"XPA1",4);memcpy(ack+4,wire+4,4);memcpy(ack+8,wire+16,4);
                    if(fault==2)ack[7]^=1;
                    if(fault==1)line("\r\nSEND OK\r\n");
                    line("\r\n+IPD,5:");enqueue(ack,5);line("\r\n+IPD,7:");enqueue(ack+5,7);
                }
                if(wire_len==24 && suppress_send_ok) { /* Lost reply: never retry data. */ }
                else if(wire_len==24 && fault==0) delayed_ok=ticks+6000;
                else if(fault==0) delayed_ok=ticks+1700; /* Slow but progressing frame >120s. */
                else if(wire_len!=sizeof(wire) || fault!=1)line("\r\nSEND OK\r\n");
                }
            }
        } else {
            assert(cmd_len+1<sizeof(cmd));cmd[cmd_len++]=bytes[i];cmd[cmd_len]=0;
            if(bytes[i]=='\n') {
                unsigned new_baud;
                if(host_baud!=module_baud) {cmd_len=0;continue;}
                if(!strcmp(cmd,"AT\r\n") && skip_at) {--skip_at;cmd_len=0;continue;}
                if(sscanf(cmd,"AT+UART_CUR=%u",&new_baud)==1) {module_baud=new_baud;line("\r\nOK\r\n");}
                else if(sscanf(cmd,"AT+CIPSEND=%d",&pending)==1) {
                    block_requested=pending;
                    assert(pending>0 && pending<=2048);send_calls++;
                    line("\r\nOK\r\n> ");
                }
                else if(strstr(cmd,"AT+CIPCLOSE"))line("\r\nCLOSED\r\nOK\r\n");
                else if(strstr(cmd,"AT+CWJAP=")) {
                    join_calls++;
                    if(transient_joins) {
                        transient_joins--;
                        line("\r\nWIFI CONNECTED\r\n+CWJAP:1\r\nFAIL\r\n");
                    }
                    else if(join_failure)line("\r\nWIFI DISCONNECT\r\n+CWJAP:2\r\nFAIL\r\n");
                    else line("\r\nWIFI DISCONNECT\r\nWIFI CONNECTED\r\nWIFI GOT IP\r\nOK\r\n");
                }
                else line("\r\nOK\r\n");
                cmd_len=0;
            }
        }
    }
    return (int)n;
}
static unsigned char pixels[IMAGE_BYTES];
static unsigned char *showbuffer=pixels;
static int image_width=320,image_height=240;
#include "actual_crc.h"
#include "photo_bridge.h"
#include "photo_orientation.h"
int main(int argc,char **argv) {
    char escaped[40]; (void)argc;
    /* Asymmetric 3x2 image: reverse pixel positions, NOT RGB565 byte order.
     * All RGB planes reverse identically; two padding bytes remain intact. */
    unsigned char display[]={1,101,2,102,3,103,4,104,5,105,6,106};
    unsigned char rgb[]={1,2,3,4,5,6,99,98,11,12,13,14,15,16,97,96,21,22,23,24,25,26,95,94};
    photo_orient_frame(display,rgb,3,2,8);
    for(int i=0;i<6;i++) {
        assert(display[2*i]==6-i && display[2*i+1]==106-i);
        assert(rgb[i]==6-i && rgb[8+i]==16-i && rgb[16+i]==26-i);
    }
    assert(rgb[6]==99 && rgb[7]==98 && rgb[14]==97 && rgb[15]==96 && rgb[22]==95 && rgb[23]==94);
    puts("PASS camera 180: asymmetric RGB565 pixels + planar RGB + padding");
    for(size_t i=0;i<sizeof(pixels);i++)pixels[i]=(unsigned char)i;
    pw_fd=1; module_baud=57600; skip_at=1;
    assert(pw_probe()==0 && pw_actual_baud==57600 && skip_at==0);
    /* First real transfer must also discover 57600 and switch to target baud. */
    photo_init();
    assert(!pw_quote(escaped,sizeof(escaped),"a,b\"c\\d") && !strcmp(escaped,"a\\,b\\\"c\\\\d"));
    assert(pw_quote(escaped,sizeof(escaped),"bad\ncommand")==-1);
    for(fault=0;fault<4;fault++) {
        if(!fault) transient_joins=2; /* Reconnect without another EN reset. */
        send_calls=0;
        wire_len=0;qr=qw=0;pending=0;cmd_len=0;pw_online=0;
        for(int i=0;i<10;i++)photo_on_detection(0);
        photo_on_detection(1);assert(photo_state==1);
        photo_send_frame();assert(photo_state==(fault>=2 ? 0:4));
        if(!fault) assert((uint32_t)(ticks-pw_transfer_start)>120000);
        assert(wire_len==sizeof(wire) && !memcmp(wire,"XPH1",4) && !memcmp(wire+24,pixels,sizeof(pixels)));
        assert(send_calls==1+(IMAGE_BYTES+PHOTO_WIFI_BLOCK_BYTES-1)/PHOTO_WIFI_BLOCK_BYTES);
        if(!fault) assert(join_calls==3 && module_baud==PHOTO_WIFI_BAUD);
        photo_show_saved(3);assert(photo_state==0 && toast==(fault ? 2:1) && lcd_writes==(fault ? 160:80));
        photo_on_detection(1);assert(photo_state==0); /* no repeats */
        if(!fault){FILE *f=fopen(argv[1],"wb");assert(f);assert(fwrite(wire,1,wire_len,f)==wire_len);fclose(f);}
    }
    pw_reset_parser();
    wire_len=0;qr=qw=0;pending=0;cmd_len=0;pw_online=0;suppress_send_ok=1;
    assert(photo_wifi_transfer(5,pixels,320,240)==-1);
    assert(wire_len==24); /* Header sent once, no blind retry or following payload. */
    suppress_send_ok=0;
    pw_transfer_start=ticks-PHOTO_WIFI_FRAME_TIMEOUT_MS-1;
    assert(pw_send(pixels,256)==-1);
    assert(!strcmp(pw_stage,"frame deadline") && wire_len==24);
    pw_reset_parser();
    wire_len=0;qr=qw=0;pending=0;cmd_len=0;pw_online=0;close_payload=1;
    assert(photo_wifi_transfer(6,pixels,320,240)==-1);
    assert(wire_len==24+PHOTO_WIFI_BLOCK_BYTES && pw_block_received==PHOTO_WIFI_BLOCK_BYTES);
    assert(!strcmp(pw_stage,"SEND OK")); /* CLOSED must not count as success or resend. */
    close_payload=0;
    pw_reset_parser();
    pw_stage="send binary block";
    for(const char *p="WIFI DISCONNECT\r\n";*p;p++) {
        int event=pw_parse(*p);if(!p[1])assert(event==PW_ERROR);
    }
    join_failure=1; pw_reset_parser(); qr=qw=0;
    assert(pw_command("AT+CWJAP=\"test\",\"hidden\"",1000)==-1);
    assert(pw_join_code==2 && !pw_joining);
    int before_joins=join_calls;
    pw_online=0;pw_reset_parser();qr=qw=0;
    assert(pw_connect()==-1 && join_calls==before_joins+1); /* Password error: no retry. */
    pw_reset_parser();
    for(const char *p="+IPD,999:";*p;p++) {int event=pw_parse(*p);if(!p[1])assert(event==PW_ERROR);}
    puts("PASS Wi-Fi firmware: fragmented AT/IPD, short writes, binary framing, matching/wrong/missing ACK, latch, LCD 2s");
    return 0;
}
