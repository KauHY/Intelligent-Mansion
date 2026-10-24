/* PC photo protocol v1, edu-riscv64 XiZi only. Included in the detector TU so
 * camera ownership stays with its existing worker. No second UART reader. */
#ifndef XI_PHOTO_BRIDGE_H
#define XI_PHOTO_BRIDGE_H
#if defined(ADD_XIZI_FEATURES) && defined(BSP_USING_LCD)
#include <shell.h>
#include "photo_toast_font.h"
#include "photo_status_font.h"
static pthread_mutex_t photo_mutex;
static volatile int photo_ready;
static unsigned int photo_id;
/* 0 idle, 1 requested, 2 sending, 3 awaiting saved, 4 saved */
static int photo_state;
/* PC result JSON stage-2 storage: photo_ethernet.h fills the buffer, then
 * photo_json_result() extracts the status code used to pick the toast. */
unsigned char photo_json[1024];
static int photo_status; /* PC check-in status code, 0 = no/default toast */
static int photo_json_result(const char *json, size_t len);
#if defined(PHOTO_WIFI_TCP) && defined(PHOTO_ETHERNET_TCP)
#error "Select only one photo network transport"
#endif
#ifdef PHOTO_ETHERNET_TCP
#include "photo_ethernet.h"
#elif defined(PHOTO_WIFI_TCP)
#include "photo_wifi.h"
#endif
/* Minimal extraction of the PC result JSON (docs 09 §7.4). The PC emits a
 * flat one-line object with ASCII keys, so a tiny scanner finds "status"
 * without pulling cJSON (whose library is not enabled in this build) into
 * the detector TU. Anything unparsable keeps status 0 -> default toast. */
static int photo_json_result(const char *json, size_t len)
{
    const char *key = "\"status\"";
    const char *end = json + len;
    const char *p;
    photo_status = 0; /* any failure leaves the default toast */
    if (!json || len < 9) return -1;
    for (p = json; p <= end - 8; p++) {
        if (memcmp(p, key, 8)) continue;
        p += 8;
        while (p < end && (*p == ' ' || *p == ':')) p++;
        if (p >= end || *p < '0' || *p > '9') return -1;
        photo_status = 0;
        while (p < end && *p >= '0' && *p <= '9') {
            if (photo_status < 100) photo_status = photo_status * 10 + (*p - '0');
            p++;
        }
        return 0;
    }
    return -1;
}
#if defined(PHOTO_WIFI_TCP) || defined(PHOTO_ETHERNET_TCP)
#define PHOTO_NETWORK_TCP
#ifndef PHOTO_FACE_CONFIRM_FRAMES
#define PHOTO_FACE_CONFIRM_FRAMES 5
#endif
#ifndef PHOTO_FACE_ABSENT_FRAMES
#ifdef PHOTO_WIFI_TCP
#define PHOTO_FACE_ABSENT_FRAMES PHOTO_WIFI_ABSENT_FRAMES
#else
#define PHOTO_FACE_ABSENT_FRAMES 10
#endif
#endif
#if PHOTO_FACE_CONFIRM_FRAMES < 1 || PHOTO_FACE_ABSENT_FRAMES < 1
#error "Photo detection frame counts must be positive"
#endif
static unsigned int photo_present, photo_absent;
static int photo_armed=1;
#endif

static void photo_on_detection(unsigned int count)
{
#ifdef PHOTO_NETWORK_TCP
    if (!photo_ready) return;
    if (!count) {
        photo_present=0;
        if (photo_absent < PHOTO_FACE_ABSENT_FRAMES) ++photo_absent;
        if (photo_absent >= PHOTO_FACE_ABSENT_FRAMES) photo_armed=1;
    } else {
        photo_absent=0;
        if (photo_present < PHOTO_FACE_CONFIRM_FRAMES) ++photo_present;
        if (photo_present >= PHOTO_FACE_CONFIRM_FRAMES && photo_armed && photo_state==0) {
            photo_armed=0;
            if (++photo_id==0) ++photo_id;
            photo_state=1;
        }
    }
#else
    (void)count;
#endif
}

static void photo_command(int argc, char **argv, int saved)
{
#ifdef PHOTO_NETWORK_TCP
    (void)argc; (void)argv; (void)saved;
    printf("[photo] TCP mode: serial photo commands disabled\n");
#else
    char *end;
    unsigned long id;
    if (!photo_ready || argc != 2) return;
    id = strtoul(argv[1], &end, 10);
    if (!*argv[1] || *end || id == 0 || id > 0x7fffffffUL) return;
    PrivMutexObtain(&photo_mutex);
    if (saved) {
        if (photo_state == 3 && photo_id == id) photo_state = 4;
    } else if (photo_state == 0 || photo_state == 3) {
        photo_id = (unsigned int)id;
        photo_state = 1;
    }
    PrivMutexAbandon(&photo_mutex);
#endif
}
static int photo_capture(int argc, char **argv) { photo_command(argc, argv, 0); return 0; }
static int photo_saved(int argc, char **argv) { photo_command(argc, argv, 1); return 0; }
SHELL_EXPORT_CMD(SHELL_CMD_PERMISSION(0)|SHELL_CMD_TYPE(SHELL_TYPE_CMD_MAIN),
                 photo_capture, photo_capture, capture camera frame for PC);
SHELL_EXPORT_CMD(SHELL_CMD_PERMISSION(0)|SHELL_CMD_TYPE(SHELL_TYPE_CMD_MAIN),
                 photo_saved, photo_saved, confirm PC photo saved);

static void photo_init(void)
{
    if (PrivMutexCreate(&photo_mutex, NULL) == 0) photo_ready = 1;
    else printf("[photo] mutex initialization failed\n");
#ifdef PHOTO_NETWORK_TCP
    printf("[photo] confirm=%u consecutive frames, rearm=%u absent frames\n",
           (unsigned)PHOTO_FACE_CONFIRM_FRAMES, (unsigned)PHOTO_FACE_ABSENT_FRAMES);
#endif
#ifdef PHOTO_ETHERNET_TCP
    printf("[photo-ethernet] W5500 TCP enabled\n");
#elif defined(PHOTO_WIFI_TCP)
    printf("[photo-wifi] enabled; awaiting face; UART1 dedicated to ESP8285\n");
#endif
}

/* Called after camera/KPU completion and BEFORE detection boxes are painted.
 * The capture worker cannot start another DVP shot until transmission ends. */
static void photo_send_frame(void)
{
    unsigned int id = 0;
    if (!photo_ready) return;
    PrivMutexObtain(&photo_mutex);
    if (photo_state == 1) { id = photo_id; photo_state = 2; }
    PrivMutexAbandon(&photo_mutex);
    if (!id) return;
#ifdef PHOTO_NETWORK_TCP
#ifdef PHOTO_ETHERNET_TCP
    int result=photo_ethernet_transfer(id,showbuffer,image_width,image_height);
#else
    int result=photo_wifi_transfer(id,showbuffer,image_width,image_height);
#endif
    PrivMutexObtain(&photo_mutex);
    photo_state=result==0 ? 4 : 0;
    PrivMutexAbandon(&photo_mutex);
    if (result) photo_status = 0; /* no JSON this round: default toast */
#else
    size_t size = (size_t)image_width * image_height * 2;
    printf("\n@PHOTO BEGIN %u %d %d %u RGB565LE %08x\n", id,
           image_width, image_height, (unsigned int)size,
           (unsigned int)model_crc32(showbuffer, size));
    for (size_t offset = 0; offset < size; offset += 64) {
        static const char hex[] = "0123456789abcdef";
        char line[192];
        int n = snprintf(line, sizeof(line), "\n@PHOTO DATA %u %u ", id, (unsigned int)offset);
        size_t count = size - offset;
        if (count > 64) count = 64;
        for (size_t i = 0; i < count; i++) {
            unsigned char b = showbuffer[offset + i];
            line[n++] = hex[b >> 4]; line[n++] = hex[b & 15];
        }
        line[n++] = '\n'; line[n] = 0;
        printf("%s", line);
        PrivTaskDelay(1); /* let console RX/shell and other tasks run */
    }
    PrivMutexObtain(&photo_mutex);
    photo_state = 3;
    PrivMutexAbandon(&photo_mutex);
    printf("\n@PHOTO END %u\n", id);
    fflush(stdout);
#endif
}

/* Rendering stays in the LCD owner's thread. Bitmaps are the pre-baked
 * status phrases (photo_status_font.h) selected by the PC result status,
 * avoiding an ASCII-only LCD font or a large CJK dependency. */
static void photo_show_saved(int lcd_fd)
{
    int show = 0;
    int status;
    const photo_bitmap_t *bm;
    if (!photo_ready) return;
    PrivMutexObtain(&photo_mutex);
    if (photo_state == 4) { photo_state = 0; show = 1; }
    PrivMutexAbandon(&photo_mutex);
    if (!show) return;
    status = photo_status;
    photo_status = 0; /* consumed: the next round starts with the default */
    bm = &photo_status_fonts[photo_status_bitmap(status)];
    uint16_t row[LCD_SIZE];
    LcdWriteParam p = {0};
    p.type = LCD_DOT_TYPE;
    p.pixel_info.pixel_color = row;
    p.pixel_info.x_startpos = 0;
    p.pixel_info.x_endpos = LCD_SIZE - 1;
    for (int y = 0; y < bm->h + 12; y++) {
        for (int x = 0; x < LCD_SIZE; x++) {
            int gx = x - (LCD_SIZE - bm->w) / 2, gy = y - 6;
            int ink = gx >= 0 && gx < bm->w && gy >= 0 && gy < bm->h &&
                (bm->bits[gy * bm->w / 8 + gx / 8] & (0x80 >> (gx % 8)));
            row[x] = ink ? 0xffff : 0x0000;
        }
        p.pixel_info.y_startpos = LCD_SIZE - bm->h - 12 + y;
        p.pixel_info.y_endpos = p.pixel_info.y_startpos;
        PrivWrite(lcd_fd, &p, NULL_PARAMETER);
    }
    PrivTaskDelay(2000);
    memset(row, 0, sizeof(row));
    for (int y = LCD_SIZE - bm->h - 12; y < LCD_SIZE; y++) {
        p.pixel_info.y_startpos = p.pixel_info.y_endpos = y;
        PrivWrite(lcd_fd, &p, NULL_PARAMETER);
    }
    printf("\n[photo] saved toast complete status=%d\n", status);
}
#else
#define photo_init() ((void)0)
#define photo_on_detection(count) ((void)0)
#define photo_send_frame() ((void)0)
#define photo_show_saved(fd) ((void)0)
#endif
#endif
