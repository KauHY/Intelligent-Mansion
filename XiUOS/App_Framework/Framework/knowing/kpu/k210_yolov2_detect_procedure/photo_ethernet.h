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

static void pe_u32(unsigned char *p, uint32_t v)
{
    p[0]=v>>24; p[1]=v>>16; p[2]=v>>8; p[3]=v;
}

static int pe_expired(uint32_t start, uint32_t limit)
{
    return (uint32_t)(PrivGetTickTime()-start) >= limit;
}

static uint32_t pe_read_u32(const unsigned char *p)
{
    return ((uint32_t)p[0] << 24) | ((uint32_t)p[1] << 16) |
           ((uint32_t)p[2] << 8) | (uint32_t)p[3];
}

/* Read exactly size bytes.  A FIN after all bytes were received is harmless;
 * a FIN before that point is a truncated result and must not be displayed. */
static int pe_recv_exact(unsigned char *data, size_t size, uint32_t timeout_ms)
{
    size_t received = 0;
    uint32_t start = PrivGetTickTime();
    while (received < size) {
        int32_t n;
        if (pe_expired(start, timeout_ms)) return -1;
        n = wiz_sock_recv(PHOTO_ETH_SOCKET, data + received,
                          (uint16_t)(size - received));
        if (n == SOCK_BUSY) { PrivTaskDelay(1); continue; }
        if (n <= 0 || n > (int32_t)(size - received)) return -1;
        received += (size_t)n;
    }
    return 0;
}

static const char *pe_json_value(const char *json, const char *key)
{
    const char *p = strstr(json, key);
    if (!p) return NULL;
    p += strlen(key);
    while (*p == ' ' || *p == '\t' || *p == '\r' || *p == '\n') ++p;
    return *p == ':' ? p + 1 : NULL;
}

static int pe_json_status(const char *json, int *status)
{
    const char *p = pe_json_value(json, "\"status\"");
    int value = 0, digits = 0;
    if (!p) return -1;
    while (*p == ' ' || *p == '\t') ++p;
    while (*p >= '0' && *p <= '9') { value = value * 10 + (*p++ - '0'); ++digits; }
    if (!digits) return -1;
    *status = value;
    return 0;
}

static int pe_json_string(const char *json, const char *key, char *out, size_t out_size)
{
    const char *p = pe_json_value(json, key);
    size_t n = 0;
    if (!p || !out_size) return -1;
    while (*p == ' ' || *p == '\t') ++p;
    if (*p++ != '\"') return -1;
    while (*p && *p != '\"') {
        if (*p == '\\' || n + 1 >= out_size) return -1;
        out[n++] = *p++;
    }
    if (*p != '\"') return -1;
    out[n] = 0;
    return 0;
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
    unsigned char header[24], expected[12], ack[12], json_size[4];
    static unsigned char json[PHOTO_ETH_JSON_MAX_BYTES + 1];
    uint8_t peer[4] = PHOTO_ETH_PC;
    wiz_NetInfo net = { .mac=PHOTO_ETH_MAC, .ip=PHOTO_ETH_IP,
        .sn=PHOTO_ETH_NETMASK, .gw=PHOTO_ETH_GATEWAY, .dns={0,0,0,0},
        .dhcp=NETINFO_STATIC };
    size_t size = (size_t)width * height * 2, received = 0;
    uint32_t json_length;
    int status;
    char message[96] = {0}, board_text[128] = {0};
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
    /* ACK proves the image reached the PC.  The same TCP connection now
     * carries a 4-byte big-endian result length followed by UTF-8 JSON. */
    stage = "PC result length";
    if (pe_recv_exact(json_size, sizeof(json_size), PHOTO_ETH_JSON_TIMEOUT_MS)) goto json_fail;
    json_length = pe_read_u32(json_size);
    if (!json_length || json_length > PHOTO_ETH_JSON_MAX_BYTES) goto json_fail;
    stage = "PC result JSON";
    if (pe_recv_exact(json, json_length, PHOTO_ETH_JSON_TIMEOUT_MS)) goto json_fail;
    json[json_length] = 0;
    if (pe_json_status((const char *)json, &status)) goto json_fail;
    (void)pe_json_string((const char *)json, "\"message\"", message, sizeof(message));
    (void)pe_json_string((const char *)json, "\"boardText\"", board_text, sizeof(board_text));
    photo_result_set(status, message, board_text);
    /* Closing is the result-stage confirmation.  The PC records this EOF as
     * delivered; omitting an extra ACK avoids waiting on a peer that has
     * already half-closed after sending the JSON. */
    wiz_sock_close(PHOTO_ETH_SOCKET);
    printf("[photo-ethernet] result id=%u status=%d message=%s boardText=%s\n",
           id, status, message[0] ? message : "(none)",
           board_text[0] ? board_text : "(none)");
    return 0;
json_fail:
    /* The PC already ACKed/saved the image.  Never request it again merely
     * because the optional LCD-result stage failed. */
    photo_result_set(-1, NULL, NULL);
    printf("[photo-ethernet] result unavailable stage=%s; using capture toast\n", stage);
    wiz_sock_close(PHOTO_ETH_SOCKET);
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
