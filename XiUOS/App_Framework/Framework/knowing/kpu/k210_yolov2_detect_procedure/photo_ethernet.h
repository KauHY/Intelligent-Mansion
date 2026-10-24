/* W5500 native TCP offload, owned exclusively by the detection worker.
 * Uses the same XPH1 / XPA1 binary protocol as pc_bridge/tcp_bridge.py. */
#ifndef PHOTO_ETHERNET_H
#define PHOTO_ETHERNET_H
#include "socket.h"
#include "photo_ethernet_config.h"
#if !defined(BSP_USING_W5500) || !defined(BSP_USING_SPI1) || !defined(BSP_SPI1_USING_SS0)
#error "Ethernet photos require W5500, SPI1 and SPI1 SS0"
#endif
#if PHOTO_ETH_SOCKET < 0 || PHOTO_ETH_SOCKET > 7 || PHOTO_ETH_PORT < 1 || PHOTO_ETH_PORT > 65535
#error "Invalid photo Ethernet socket or port"
#endif
extern int PhotoWiznetReady(void);

static int pe_expired(uint32_t start, uint32_t limit)
{
    return (uint32_t)(PrivGetTickTime()-start) >= limit;
}

/* One-byte receipt inside the PC's 3s window: wiz_sock_send only queues the
 * send and may report SOCK_BUSY while SENDOK is pending, and wiz_sock_close
 * drops unwritten TX, so queue and wait for the hardware to drain. Called
 * only in the JSON success path; failures just skip the receipt (PC logs
 * mode=eof), never delay the toast. */
static void pe_receipt(void)
{
    uint32_t start = PrivGetTickTime();
    int32_t sent;
    while ((sent = wiz_sock_send(PHOTO_ETH_SOCKET, (uint8_t *)"1", 1)) == SOCK_BUSY) {
        if (pe_expired(start, PHOTO_ETH_JSON_TIMEOUT_MS)) {
            printf("[photo-ethernet] board receipt busy timeout\n");
            return;
        }
        PrivTaskDelay(1);
    }
    if (sent != 1) {
        printf("[photo-ethernet] board receipt rejected rc=%d SR=0x%02x\n",
               (int)sent, (unsigned)getSn_SR(PHOTO_ETH_SOCKET));
        return;
    }
    while (getSn_TX_FSR(PHOTO_ETH_SOCKET) != getSn_TxMAX(PHOTO_ETH_SOCKET)) {
        if (pe_expired(start, PHOTO_ETH_JSON_TIMEOUT_MS)) {
            printf("[photo-ethernet] board receipt drain timeout\n");
            break;
        }
        PrivTaskDelay(1);
    }
    printf("[photo-ethernet] board receipt sent\n");
}

static void pe_u32(unsigned char *p, uint32_t v)
{
    p[0]=v>>24; p[1]=v>>16; p[2]=v>>8; p[3]=v;
}

static int pe_send(const unsigned char *data, size_t size, uint32_t start)
{
    while (size) {
        if (pe_expired(start, PHOTO_ETH_FRAME_TIMEOUT_MS)) return -1;
        /* Each hardware socket has a 2 KiB TX buffer. The driver may also
         * accept fewer bytes or return SOCK_BUSY while SENDOK is pending. */
        uint16_t n = size > 2048 ? 2048 : (uint16_t)size;
        int32_t sent = wiz_sock_send(PHOTO_ETH_SOCKET, (uint8_t *)data, n);
        if (sent < 0 || sent > n) return -1;
        if (sent == SOCK_BUSY) { PrivTaskDelay(1); continue; }
        data += sent;
        size -= sent;
    }
    return 0;
}

static int photo_ethernet_transfer(unsigned int id, const unsigned char *pixels,
                                   int width, int height)
{
    unsigned char header[24], expected[12], ack[12], json_prefix[4];
    unsigned int json_need = 0, json_got = 0;
    uint8_t peer[4] = PHOTO_ETH_PC;
    wiz_NetInfo net = { .mac=PHOTO_ETH_MAC, .ip=PHOTO_ETH_IP,
        .sn=PHOTO_ETH_NETMASK, .gw=PHOTO_ETH_GATEWAY, .dns={0,0,0,0},
        .dhcp=NETINFO_STATIC };
    size_t size = (size_t)width * height * 2, received = 0;
    uint32_t crc = model_crc32(pixels, size), start = PrivGetTickTime();
    const char *stage = "configure network", *reason = "operation failed";
    int rc = 0;
    if (!PhotoWiznetReady()) {
        printf("[photo-ethernet] W5500 initialization failed; no transfer\n");
        return -1;
    }
    uint8_t version = getVERSIONR(), phy = getPHYCFGR();
    printf("\n[photo-ethernet v2] id=%u chip=0x%02x PHY=0x%02x link=%s\n",
           id, (unsigned)version, (unsigned)phy, (phy & PHYCFGR_LNK_ON) ? "UP" : "DOWN");
    printf("[photo-ethernet] board=%u.%u.%u.%u mask=%u.%u.%u.%u gateway=%u.%u.%u.%u\n",
           net.ip[0],net.ip[1],net.ip[2],net.ip[3],net.sn[0],net.sn[1],net.sn[2],net.sn[3],
           net.gw[0],net.gw[1],net.gw[2],net.gw[3]);
    printf("[photo-ethernet] target=%u.%u.%u.%u:%u socket=%u connect_limit_ms=%u\n",
           peer[0],peer[1],peer[2],peer[3],(unsigned)PHOTO_ETH_PORT,
           (unsigned)PHOTO_ETH_SOCKET,(unsigned)PHOTO_ETH_CONNECT_TIMEOUT_MS);
    if (version != 0x04) {
        printf("[photo-ethernet] invalid W5500 version; check SPI/power/reset; no transfer\n");
        return -1;
    }
    if (!(phy & PHYCFGR_LNK_ON)) {
        printf("[photo-ethernet] cable link DOWN; check RJ45/cable/peer port; no transfer\n");
        return -1;
    }
    rc = ctlnetwork(CN_SET_NETINFO, &net);
    if (rc < 0) {
        printf("[photo-ethernet] network configuration failed rc=%d\n", rc);
        return -1;
    }
    stage = "open socket";
    rc = wiz_socket(PHOTO_ETH_SOCKET, Sn_MR_TCP, 0, SF_IO_NONBLOCK | SF_TCP_NODELAY);
    if (rc != PHOTO_ETH_SOCKET) goto fail;
    stage = "connect";
    start = PrivGetTickTime();
    rc = wiz_sock_connect(PHOTO_ETH_SOCKET, peer, PHOTO_ETH_PORT);
    if (rc < 0) { reason = "connect API error"; goto fail; }
    while (getSn_SR(PHOTO_ETH_SOCKET) != SOCK_ESTABLISHED) {
        if (!(getPHYCFGR() & PHYCFGR_LNK_ON)) { reason = "link lost"; goto fail; }
        if (getSn_IR(PHOTO_ETH_SOCKET) & Sn_IR_TIMEOUT) { reason = "hardware timeout"; goto fail; }
        if (getSn_SR(PHOTO_ETH_SOCKET) == SOCK_CLOSED) { reason = "socket closed"; goto fail; }
        if (pe_expired(start, PHOTO_ETH_CONNECT_TIMEOUT_MS)) { reason = "connect deadline"; goto fail; }
        PrivTaskDelay(1);
    }
    printf("[photo-ethernet] connected elapsed_ms=%u; sending %u bytes\n",
           (unsigned)(PrivGetTickTime()-start),(unsigned)size);
    memcpy(header, "XPH1", 4); pe_u32(header+4, id);
    header[8]=width>>8; header[9]=width; header[10]=height>>8; header[11]=height;
    pe_u32(header+12, (uint32_t)size); pe_u32(header+16, crc); pe_u32(header+20, 1);
    memcpy(expected, "XPA1", 4); pe_u32(expected+4, id); pe_u32(expected+8, crc);
    stage = "send frame";
    start = PrivGetTickTime();
    if (pe_send(header, sizeof(header), start) || pe_send(pixels, size, start)) goto fail;
    stage = "PC saved ACK";
    start = PrivGetTickTime();
    while (received < sizeof(ack)) {
        if (pe_expired(start, PHOTO_ETH_ACK_TIMEOUT_MS)) goto fail;
        int32_t n = wiz_sock_recv(PHOTO_ETH_SOCKET, ack+received,
                                  (uint16_t)(sizeof(ack)-received));
        if (n < 0 || n > (int32_t)(sizeof(ack)-received)) goto fail;
        if (n == SOCK_BUSY) { PrivTaskDelay(1); continue; }
        received += n;
    }
    if (memcmp(ack, expected, sizeof(ack))) goto fail;
    /* ACK proves the last queued send reached the PC and was saved. Stage 2
     * keeps reading the same connection: the PC answers 4B JSON length +
     * JSON, then half-closes. EOF arrives as a negative error code once the
     * RX buffer drains, so a short read is a broken stream either way; a
     * JSON failure only costs the toast text (the photo is already saved
     * and checked in), never a re-capture that could duplicate the request. */
    stage = "PC result JSON";
    start = PrivGetTickTime();
    json_got = 0;
    while (json_got < sizeof(json_prefix)) {
        if (pe_expired(start, PHOTO_ETH_JSON_TIMEOUT_MS)) goto json_fail;
        int32_t n = wiz_sock_recv(PHOTO_ETH_SOCKET, json_prefix + json_got,
                                  (uint16_t)(sizeof(json_prefix) - json_got));
        if (n == SOCK_BUSY) { PrivTaskDelay(1); continue; }
        if (n < 0 || n > (int32_t)(sizeof(json_prefix) - json_got)) goto json_fail;
        json_got += (uint32_t)n;
    }
    json_need = ((uint32_t)json_prefix[0] << 24) | ((uint32_t)json_prefix[1] << 16) |
                ((uint32_t)json_prefix[2] << 8) | (uint32_t)json_prefix[3];
    if (json_need > sizeof(photo_json)) goto json_fail;
    json_got = 0;
    while (json_got < json_need) {
        if (pe_expired(start, PHOTO_ETH_JSON_TIMEOUT_MS)) goto json_fail;
        int32_t n = wiz_sock_recv(PHOTO_ETH_SOCKET, photo_json + json_got,
                                  (uint16_t)(json_need - json_got));
        if (n == SOCK_BUSY) { PrivTaskDelay(1); continue; }
        if (n < 0 || n > (int32_t)(json_need - json_got)) goto json_fail;
        json_got += (uint32_t)n;
    }
    printf("[photo-ethernet] board result json=%u %.*s\n",
           (unsigned)json_need, (int)json_need, (const char *)photo_json);
    photo_json_result((const char *)photo_json, (size_t)json_need);
    pe_receipt();
    wiz_sock_close(PHOTO_ETH_SOCKET);
    printf("[photo-ethernet] PC saved id=%u bytes=%u json=%u\n",
           id, (unsigned)size, (unsigned)json_need);
    return 0;
json_fail:
    /* Degrade, never retry: photo_state stays 4 so the default captured
     * toast shows and no second check-in request is generated. */
    photo_status = 0; /* unknown outcome: default "photo captured" toast */
    printf("[photo-ethernet] json failed got=%u need=%u SR=0x%02x elapsed_ms=%u; default toast\n",
           (unsigned)json_got, (unsigned)json_need, (unsigned)getSn_SR(PHOTO_ETH_SOCKET),
           (unsigned)(PrivGetTickTime()-start));
    wiz_sock_close(PHOTO_ETH_SOCKET);
    printf("[photo-ethernet] PC saved id=%u bytes=%u\n", id, (unsigned)size);
    return 0;
fail:
    /* Read evidence BEFORE close clears socket status and interrupts. */
    printf("[photo-ethernet] failed stage=%s reason=%s rc=%d SR=0x%02x IR=0x%02x PHY=0x%02x elapsed_ms=%u; no toast\n",
           stage,reason,rc,(unsigned)getSn_SR(PHOTO_ETH_SOCKET),
           (unsigned)getSn_IR(PHOTO_ETH_SOCKET),(unsigned)getPHYCFGR(),
           (unsigned)(PrivGetTickTime()-start));
    wiz_sock_close(PHOTO_ETH_SOCKET);
    return -1;
}
#endif
