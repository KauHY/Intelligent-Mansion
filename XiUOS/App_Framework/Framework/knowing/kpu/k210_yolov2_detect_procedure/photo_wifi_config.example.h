#ifndef PHOTO_WIFI_CONFIG_H
#define PHOTO_WIFI_CONFIG_H
/* Copy to photo_wifi_config.h locally. Do not commit credentials. */
#define PHOTO_WIFI_SSID "YOUR_2G4_WIFI"
#define PHOTO_WIFI_PASSWORD "YOUR_PASSWORD"
#define PHOTO_WIFI_PC "192.168.1.100"
#define PHOTO_WIFI_PORT 9000
/* ESP persisted AT UART rate at power-on; unrelated to COM3/KFlash rate. */
#define PHOTO_WIFI_BOOT_BAUD 115200
/* Start at 115200. Try 921600 only after successful end-to-end validation. */
#define PHOTO_WIFI_BAUD 115200
#define PHOTO_WIFI_ENABLE_PIN 8
#define PHOTO_WIFI_ABSENT_FRAMES 10
#endif
