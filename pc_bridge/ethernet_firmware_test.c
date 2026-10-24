/* Host mocks below the real W5500 API; exercises production photo headers. */
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#define ADD_XIZI_FEATURES 1
#define BSP_USING_LCD 1
#define PHOTO_ETHERNET_TCP 1
#define BSP_USING_W5500 1
#define BSP_USING_SPI1 1
#define BSP_SPI1_USING_SS0 1
#define LCD_SIZE 320
#define LCD_DOT_TYPE 1
#define NULL_PARAMETER 0
typedef int pthread_mutex_t;
typedef struct {int type; struct {uint16_t *pixel_color; int x_startpos,x_endpos,y_startpos,y_endpos;}pixel_info;} LcdWriteParam;
static uint32_t ticks;
static int locked, toast, writes, fault, closes, calls;
static size_t wire_len, ack_pos, reply_len;
static unsigned char pixels[320*240*2], wire[sizeof(pixels)+24], reply[512];
static unsigned char *showbuffer=pixels;
static int image_width=320, image_height=240;
static uint32_t PrivGetTickTime(void) {return ticks++;}
static void PrivTaskDelay(int ms) {assert(!locked);ticks+=ms;if(ms==2000)toast++;}
static int PrivMutexCreate(int *m,void *p) {(void)m;(void)p;return 0;}
static int PrivMutexObtain(int *m) {(void)m;assert(!locked);locked=1;return 0;}
static int PrivMutexAbandon(int *m) {(void)m;assert(locked);locked=0;return 0;}
static int PrivWrite(int fd,LcdWriteParam *p,int n) {(void)fd;(void)p;(void)n;writes++;return 0;}
#include "actual_crc.h"
#include "photo_bridge.h"
int PhotoWiznetReady(void) {return fault!=7;}
int8_t ctlnetwork(ctlnetwork_type t,void *v) {(void)t;(void)v;return 0;}
uint8_t WIZCHIP_READ(uint32_t addr) {
    if(addr==VERSIONR) return fault==8 ? 0xff : 0x04;
    if(addr==PHYCFGR) return fault==9 ? 0 : PHYCFGR_LNK_ON;
    if(addr==Sn_SR(PHOTO_ETH_SOCKET)) return fault==1 ? SOCK_INIT : SOCK_ESTABLISHED;
    return 0;
}
int8_t wiz_socket(uint8_t sn,uint8_t proto,uint16_t port,uint8_t flag) {
    (void)proto;(void)port;assert(flag & SF_IO_NONBLOCK);return sn;
}
int8_t wiz_sock_connect(uint8_t sn,uint8_t *ip,uint16_t port) {(void)sn;(void)ip;(void)port;return SOCK_BUSY;}
int8_t wiz_sock_close(uint8_t sn) {(void)sn;closes++;return SOCK_OK;}
int32_t wiz_sock_send(uint8_t sn,uint8_t *data,uint16_t n) {
    (void)sn;calls++;
    if(fault==2) return SOCK_BUSY;
    if(fault==3 && wire_len>24) return SOCKERR_SOCKCLOSED;
    if(calls%3==0) return SOCK_BUSY;
    if(n>317) n=317; /* short positive writes must not duplicate/skip bytes */
    assert(wire_len+n<=sizeof(wire));memcpy(wire+wire_len,data,n);wire_len+=n;
    if(wire_len==sizeof(wire)) {
        memcpy(reply,"XPA1",4);memcpy(reply+4,wire+4,4);memcpy(reply+8,wire+16,4);
        if(fault==4) reply[7]^=1;
        if(fault==5) reply[11]^=1;
        const char *json = "{\"status\":1,\"message\":\"签到成功\",\"boardText\":\"张三 签到成功\"}";
        uint32_t json_len=(uint32_t)strlen(json);
        reply[12]=(unsigned char)(json_len>>24);reply[13]=(unsigned char)(json_len>>16);
        reply[14]=(unsigned char)(json_len>>8);reply[15]=(unsigned char)json_len;
        memcpy(reply+16,json,json_len);reply_len=16+json_len;
    }
    return n;
}
int32_t wiz_sock_recv(uint8_t sn,uint8_t *data,uint16_t n) {
    (void)sn;assert(wire_len==sizeof(wire));
    if(fault==6) return SOCK_BUSY;
    if(n>2) n=2;
    assert(ack_pos+n<=reply_len);
    memcpy(data,reply+ack_pos,n);ack_pos+=n;return n;
}
static void detect(unsigned n,int count) {while(n--) photo_on_detection(count);}
int main(int argc,char **argv) {
    assert(argc==2);photo_init();
    for(size_t i=0;i<sizeof(pixels);i++)pixels[i]=(unsigned char)i;
    detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
    detect(1,0);detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
    /* Reset interrupted sequence before testing success/failure episodes. */
    for(fault=0;fault<=9;fault++) {
        detect(PHOTO_FACE_ABSENT_FRAMES,0);
        wire_len=ack_pos=reply_len=0;calls=0;
        detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
        detect(1,1);assert(photo_state==1);
        int before=closes;
        photo_send_frame();assert(photo_state==(fault ? 0:4));
        assert(closes==before+(fault>=7 ? 0:1));
        if(fault>=7) assert(calls==0 && wire_len==0);
        photo_show_saved(1);assert(toast==1 && writes==80 && photo_state==0);
        detect(200,1);assert(photo_state==0); /* latch even after failed transfer */
        detect(PHOTO_FACE_ABSENT_FRAMES-1,0);detect(20,1);assert(photo_state==0);
        if(!fault) {
            assert(wire_len==sizeof(wire) && !memcmp(wire+24,pixels,sizeof(pixels)));
            FILE *f=fopen(argv[1],"wb");assert(f);assert(fwrite(wire,1,wire_len,f)==wire_len);fclose(f);
        }
    }
    /* All board-facing status categories select a fixed, known glyph. */
    photo_result_set(1,"签到成功","张三 签到成功");assert(photo_toast==PHOTO_TOAST_SUCCESS);
    photo_result_set(2,"已签到","张三 已签到");assert(photo_toast==PHOTO_TOAST_DUPLICATE);
    photo_result_set(3,"无权限","张三 无权限");assert(photo_toast==PHOTO_TOAST_NO_PERMISSION);
    photo_result_set(4,"未识别","未识别");assert(photo_toast==PHOTO_TOAST_NOT_RECOGNIZED);
    photo_result_set(5,"签到未开始","签到未开始");assert(photo_toast==PHOTO_TOAST_NOT_STARTED);
    photo_result_set(5,"签到已结束","签到已结束");assert(photo_toast==PHOTO_TOAST_ENDED);
    photo_result_set(7,"处理失败","暂不可用");assert(photo_toast==PHOTO_TOAST_UNAVAILABLE);
    ticks=UINT32_MAX-5;uint32_t start=PrivGetTickTime();PrivTaskDelay(20);
    assert(pe_expired(start,10));
    puts("PASS Ethernet: consecutive frames/reset/rearm, partial sends/ACK, connect/send/ACK timeouts, disconnect, wrong ID/CRC, init failure, LCD, tick wrap");
    return 0;
}
