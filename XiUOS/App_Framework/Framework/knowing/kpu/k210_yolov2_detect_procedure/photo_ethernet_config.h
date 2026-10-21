/* Static IPv4 example for a direct cable or a dedicated LAN.
 * Change these addresses to match the PC's wired adapter before flashing. */
#ifndef PHOTO_ETHERNET_CONFIG_H
#define PHOTO_ETHERNET_CONFIG_H
#define PHOTO_ETH_IP       {192, 168, 50, 2}
#define PHOTO_ETH_NETMASK  {255, 255, 255, 0}
#define PHOTO_ETH_GATEWAY  {0, 0, 0, 0}
#define PHOTO_ETH_PC       {192, 168, 50, 1}
#define PHOTO_ETH_MAC      {0x02, 0x58, 0x49, 0x55, 0x00, 0x01}
#define PHOTO_ETH_PORT 9000
/* Reserved exclusively for photo capture; do not run W5500 demos concurrently. */
#define PHOTO_ETH_SOCKET 2
#define PHOTO_ETH_CONNECT_TIMEOUT_MS 10000U
#define PHOTO_ETH_FRAME_TIMEOUT_MS 30000U
#define PHOTO_ETH_ACK_TIMEOUT_MS 10000U
#endif
