# W5500 有线人像采集

适用于 `XiUOS/Ubiquitous/XiZi_IIoT` 的 `edu-riscv64`，使用板载 W5500 RJ45 网口。

## 采集行为

- 默认连续 **5 帧**完成采集和 KPU 检测且都有目标，才上传第 5 帧的完整图像，上传前尚未画检测框。
- 任意一帧无目标，连续有人计数归零。保持有人只采集一次；连续 **10 帧**无目标后重新允许触发。失败也需要先离开画面再进入，避免网络故障时反复阻塞检测。
- 不缓存或上传前 4 帧。传输和两秒成功提示期间仍暂停检测，沿用原有摄像头缓冲区所有权。
- 保持使用 FACE 模型，条件是目标数大于 0；不进行身份跟踪，不能保证连续几帧是同一个人。
- 沿用 XPH1/XPA1 协议。主机校验 CRC32、保存 PNG 后回传图片 ID 和 CRC；匹配后才显示采集成功。

## 网线与地址配置

板和 PC 可以连接同一交换机，或者网线直连 PC 有线网卡。默认配置为独立网段示例：

| 项目 | 默认值 |
| --- | --- |
| PC 有线 IPv4 | 192.168.50.1 |
| 板端 IPv4 | 192.168.50.2 |
| 子网掩码 | 255.255.255.0 |
| 网关 | 0.0.0.0（同网段直连无需网关） |
| TCP 端口 | 9000 |

在 Windows 手动配置有线适配器地址，或按已有局域网修改：

`XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_ethernet_config.h`

`PHOTO_ETH_PC` 填 Windows **有线网卡**地址；`PHOTO_ETH_IP` 填板端独立地址。多块板应使用不同 IP 和 `PHOTO_ETH_MAC`，避免冲突。没有启用 DHCP；每次传图会应用上述配置，覆盖 shell 临时网络配置。

双击根目录 `Start-Ethernet-Photo.cmd`，等到 `[监听 TCP] 0.0.0.0:9000`。Python 3.10+ 即可，无额外依赖。照片保存到 `tmp_photos/`。等价命令：

```powershell
.\.venv\Scripts\python.exe -u pc_bridge/tcp_bridge.py --port 9000
```

允许 Windows 防火墙中的 Python/TCP 9000 入站连接，规则应适用于当前有线网络配置文件。可以同时使用 COM3 查看日志，不运行串口照片接收 `bridge.py`。

## 同步源码、编译与烧录

在原有已调通 YOLO 的虚拟机代码树中同步：

- 检测目录的 `Kconfig`、`photo_bridge.h`、`photo_ethernet.h`、`photo_ethernet_config.h`。
- `Ubiquitous/XiZi_IIoT/board/edu-riscv64/third_party_driver/ethernet/connect_w5500.c`，包含新增初始化状态接口，必须同步以免链接失败。

仓库根 `.config` 和板级默认配置不是完整的已验证摄像头配置，**不要覆盖原有可运行 YOLO 的配置**。先备份原环境的 `.config`、`xsconfig.h` 和 `board/edu-riscv64/.config`。此项目的 menuconfig 会先从板级 `.config` 复制配置，若已验证配置仅存在于根 `.config`，应在备份后先将它同步至板级 `.config`。在 `XiUOS/Ubiquitous/XiZi_IIoT` 执行 `make BOARD=edu-riscv64 menuconfig`，保留摄像头、KPU、LCD、FACE 模型、POSIX 和互斥量配置，并检查：

```text
CONFIG_BSP_USING_WIZCHIP=y
CONFIG_BSP_USING_W5500=y
CONFIG_BSP_USING_SPI=y
CONFIG_BSP_USING_SPI1=y
CONFIG_BSP_SPI1_USING_SS0=y
CONFIG_PHOTO_ETHERNET_TCP=y
# CONFIG_PHOTO_WIFI_TCP is not set
CONFIG_PHOTO_FACE_CONFIRM_FRAMES=5
CONFIG_PHOTO_FACE_ABSENT_FRAMES=10
# CONFIG_SEPARATE_COMPILE is not set
```

有线照片选项满足依赖时默认开启；确认最终保存配置已关闭 Wi-Fi。帧数范围为 1–120，确认帧数设为 1 即恢复单帧触发。Wi-Fi 模式也使用这两个新参数；旧 `PHOTO_WIFI_ABSENT_FRAMES` 只在没有新宏时作为回退。

沿用板级引脚：SPI1 CLK=9、D0=11、D1=10、SS0=12，W5500 RST=13、INT=14。不要运行占用 SPI1 的其他设备示例。当前驱动使用 10 MHz SPI，网口链路速率不等于图像实际吞吐量。

W5500 socket 2 和网络配置由采集程序独占，不同时运行 `wiz_client_op`、`wiz_server_op`、`wiz_udp`、iperf 或其他网口测试。有线模式不初始化 ESP8285，不需要 Wi-Fi 密码或 `photo_wifi_config.h`。

保存后执行 `make BOARD=edu-riscv64`，按原流程用 K-Flash 烧录新生成固件。只更新 PC 程序不会改变板端行为。

## 实板验收

1. 先启动 PC 接收端，再复位板。确认 W5500 初始化无错误，并出现 `[photo-ethernet] W5500 TCP enabled` 和 `confirm=5`。
2. 人脸出现不足 5 帧不应上传，连续满足条件才上传一次。检查 PNG 内容、方向及 LCD 两秒提示。
3. 持续有人不重复上传；离开至少 10 个检测帧再进入，应再次上传。
4. 拔网线或停止接收端，不应显示成功；恢复网络并离开后重新进入可再次采集。

应用层连接等待 10 秒、发送等待 30 秒、保存确认等待 10 秒。使用非阻塞 W5500 TCP API 处理忙状态、短写和分片确认。底层原驱动仍有硬件寄存器轮询，SPI/芯片故障需实板检查。

## 验证结果

已通过：真实固件头文件配合仓库 W5500 API 头文件的 GCC 编译（警告视为错误）、连续帧中断重计数及重新触发、短写、分片 ACK、错误 ID/CRC、连接/发送/ACK 超时、断连、初始化失败、计时回绕、LCD 提示测试。C 生成的 320×240 数据包通过 Python TCP 接收器保存 PNG 并确认。原 Wi-Fi、串口固件主机测试及 16 项 PC 接收测试通过。

```powershell
.\.venv\Scripts\python.exe pc_bridge/test_ethernet_firmware.py "D:/Software Install/CodeBlocks/MinGW/bin/gcc.exe"
cd pc_bridge
..\.venv\Scripts\python.exe -m unittest test_bridge test_tcp_bridge -v
```

当前环境无可用 RISC-V 交叉编译工具链，尚未生成或烧录新 BIN，也没有实板测速结果。主机模拟测试速度不能作为开发板速度。
