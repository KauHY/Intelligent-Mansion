/* Host mocks below the real W5500 API; exercises production photo headers.
 * Stage 2 (PC result JSON) is simulated by the same wire stream: after the
 * 12B ACK the mock appends 4B length + JSON, and wiz_sock_recv serves it. */
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
static int locked, toast, writes, fault, closes, calls, receipt, json_mode;
static size_t wire_len, ack_pos;
static unsigned char pixels[320*240*2], wire[sizeof(pixels)+24+4+256], reply[12];
static unsigned char *showbuffer=pixels;
static int image_width=320, image_height=240;
static const char *board_json = "{\"status\":1,\"message\":\"ok\",\"personName\":\"\xe5\xbc\xa0\xe4\xb8\x89\","
    "\"employeeNo\":\"1001\",\"boardText\":\"\xe5\xbc\xa0\xe4\xb8\x89 \xe7\xad\xbe\xe5\x88\xb0\xe6\x88\x90\xe5\x8a\x9f\"}";
static uint32_t PrivGetTickTime(void) {return ticks++;}
static void PrivTaskDelay(int ms) {assert(!locked);ticks+=ms;if(ms==2000)toast++;}
static int PrivMutexCreate(int *m,void *p) {(void)m;(void)p;return 0;}
static int PrivMutexObtain(int *m) {(void)m;assert(!locked);locked=1;return 0;}
static int PrivMutexAbandon(int *m) {(void)m;assert(!locked==0&&locked);locked=0;return 0;}
static int PrivWrite(int fd,LcdWriteParam *p,int n) {(void)fd;(void)p;(void)n;writes++;return 0;}
#include "actual_crc.h"
#include "photo_bridge.h"
int PhotoWiznetReady(void) {return fault!=7;}
int8_t ctlnetwork(ctlnetwork_type t,void *v) {(void)t;(void)v;return 0;}
uint8_t WIZCHIP_READ(uint32_t addr) {
    if(addr==VERSIONR) return fault==8 ? 0xff : 0x04;
    if(addr==PHYCFGR) return fault==9 ? 0 : PHYCFGR_LNK_ON;
    if(addr==Sn_TXBUF_SIZE(PHOTO_ETH_SOCKET)) return 2; /* getSn_TxMAX -> 2048 */
    if(addr==Sn_SR(PHOTO_ETH_SOCKET)) {
        if(fault==1) return SOCK_INIT;
        /* CLOSE_WAIT after the PC half-closes, mirroring the real driver */
        if(json_mode && ack_pos>=4) return SOCK_CLOSE_WAIT;
        return SOCK_ESTABLISHED;
    }
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
    /* Board receipt byte: account it, keep it off the photo wire stream.
     * It is the only 1-byte send issued once the photo is fully written. */
    if(json_mode && n==1 && wire_len==24+sizeof(pixels)) {
        assert(data[0]=='1');receipt=1;return n;
    }
    assert(wire_len+n<=sizeof(wire));memcpy(wire+wire_len,data,n);wire_len+=n;
    if(wire_len==24+sizeof(pixels)) {
        memcpy(reply,"XPA1",4);memcpy(reply+4,wire+4,4);memcpy(reply+8,wire+16,4);
        if(fault==4) reply[7]^=1;
        if(fault==5) reply[11]^=1;
        if(json_mode) { /* PC side: append 4B length + JSON after the ACK */
            uint32_t jl=(uint32_t)strlen(board_json);
            wire[24+sizeof(pixels)+0]=(unsigned char)(jl>>24);
            wire[24+sizeof(pixels)+1]=(unsigned char)(jl>>16);
            wire[24+sizeof(pixels)+2]=(unsigned char)(jl>>8);
            wire[24+sizeof(pixels)+3]=(unsigned char)jl;
            memcpy(wire+24+sizeof(pixels)+4,board_json,jl);
        }
    }
    return n;
}
int32_t wiz_sock_recv(uint8_t sn,uint8_t *data,uint16_t n) {
    (void)sn;assert(wire_len>=24+sizeof(pixels));
    if(fault==6) return SOCK_BUSY;
    /* Stage 1 serves the 12B ACK; stage 2 (json_mode) then serves the PC's
     * 4B length + JSON stream, ending with the real driver's EOF error. */
    size_t served = 12;
    const unsigned char *src = reply;
    size_t pos = ack_pos;
    if(json_mode && ack_pos>=12) { src = wire+24+sizeof(pixels); pos = ack_pos-12; }
    if(json_mode) served = 12+4+strlen(board_json);
    if(pos>=served-(json_mode ? 12:0)) return SOCKERR_SOCKSTATUS;
    if(n>served-(json_mode ? 12:0)-pos) n=(uint16_t)(served-(json_mode ? 12:0)-pos);
    if(n>2) n=2;
    memcpy(data,src+pos,n);ack_pos+=n;return n;
}
uint16_t getSn_TX_FSR(uint8_t sn) {(void)sn;return receipt ? 2048 : 0;}
/* getSn_TxMAX reads Sn_TXBUF_SIZE via WIZCHIP_READ (w5500.h:2081): serve 2KiB. */
static void detect(unsigned n,int count) {while(n--) photo_on_detection(count);}
int main(int argc,char **argv) {
    assert(argc==2);photo_init();
    for(size_t i=0;i<sizeof(pixels);i++)pixels[i]=(unsigned char)i;
    detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
    detect(1,0);detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
    /* Reset interrupted sequence before testing success/failure episodes. */
    for(fault=0;fault<=9;fault++) {
        detect(PHOTO_FACE_ABSENT_FRAMES,0);
        wire_len=ack_pos=0;calls=0;json_mode=1;
        detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);assert(photo_state==0);
        detect(1,1);assert(photo_state==1);
        int before=closes;
        photo_send_frame();assert(photo_state==(fault ? 0:4));
        assert(closes==before+(fault>=7 ? 0:1));
        if(fault>=7) assert(calls==0 && wire_len==0);
        if(!fault) {
            /* wire_len counts only the board's own TX (header+pixels); the
             * JSON payload lives beyond it in the mock's wire buffer. */
            assert(wire_len==24+sizeof(pixels));
            assert(!memcmp(wire+24,pixels,sizeof(pixels)));
            assert(!memcmp(wire+24+sizeof(pixels)+4,board_json,strlen(board_json)));
            assert(photo_status==1);
            assert(receipt==1); /* board receipt for a fully consumed JSON */
            photo_show_saved(1);assert(toast==1 && writes==80 && photo_state==0);
            assert(photo_status==0); /* consumed back to the default toast */
            detect(200,1);assert(photo_state==0); /* latch even after failed transfer */
            detect(PHOTO_FACE_ABSENT_FRAMES-1,0);detect(20,1);assert(photo_state==0);
            FILE *f=fopen(argv[1],"wb");assert(f);
            assert(fwrite(wire,1,24+sizeof(pixels),f)==24+sizeof(pixels));fclose(f);
            /* parser unit checks: keys out of order, spaces, garbage */
            photo_json_result("{\"msg\":\"x\",\"status\": 5}",30);assert(photo_status==5);
            photo_json_result("{\"status\":7,\"tail\":\"x\"",24);assert(photo_status==7);
            photo_json_result("{\"message\":\"status\"}",17);assert(photo_status==0);
            photo_json_result("no json here",12);assert(photo_status==0);
        }
        if(fault) {photo_show_saved(1);assert(toast==1 && writes==80 && photo_state==0);}
        receipt=0;
    }
    /* json_mode=0: legacy PC (ACK only, close after) -> stage 2 degrades,
     * state stays 4, default toast. */
    fault=0;json_mode=0;detect(PHOTO_FACE_ABSENT_FRAMES,0);
    wire_len=ack_pos=0;calls=0;receipt=0;
    int toast_before=toast, writes_before=writes;
    detect(PHOTO_FACE_CONFIRM_FRAMES-1,1);detect(1,1);
    int before=closes;
    photo_send_frame();
    assert(photo_state==4 && photo_status==0 && closes==before+1);
    photo_show_saved(1);
    assert(toast==toast_before+1 && writes==writes_before+80 && photo_state==0);
    puts("PASS Ethernet: consecutive frames/reset/rearm, partial sends/ACK, JSON stage2 parse+receipt, legacy-PC degradation, connect/send/ACK/JSON timeouts, disconnect, wrong ID/CRC, init failure, LCD, tick wrap");
    return 0;
}
