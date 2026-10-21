# IntelligentMansion 智能楼宇人像采集

本项目使用 edu-riscv64 开发板运行 XiUOS / XiZi，通过 K210 和内置 FACE 模型进行 YOLOv2 人脸检测。图像采用 **W5500 网口有线 TCP** 上传至 Windows 电脑，电脑保存 PNG 后回传确认，开发板显示采集成功提示。

## 采集流程

1. 连续 **5 个检测帧**都有人脸后，上传第 5 帧的完整图像；中间出现无脸帧就重新计数。
2. 图像在绘制检测框之前发送，当前尺寸为 320×240、RGB565。
3. 电脑校验图片长度和 CRC32，将 PNG 保存到项目 `tmp_photos/`。
4. 电脑回传匹配的图片 ID 和 CRC，开发板收到后在 LCD 显示“人像已完成采集”两秒。
5. 持续有人仅采集一次；连续 **10 个检测帧**无脸后，才允许再次触发。传输失败后同样需要满足此条件，或复位开发板后再试。

帧数不是秒数，实际确认时间取决于检测速度。此流程只上传一张照片，不缓存前 4 帧，也不判断连续帧是否属于同一个人。连接、传图和成功提示均在摄像头线程中执行，期间检测会暂停。

## 硬件与网络

将开发板的 W5500 RJ45 网口接到电脑有线网口，或将两端接入同一局域网交换机。当前使用静态 IPv4，不使用 DHCP。

| 项目 | 默认值 |
| --- | --- |
| 电脑有线网卡 IP | `192.168.50.1` |
| 开发板 IP | `192.168.50.2` |
| 两端子网掩码 | `255.255.255.0` |
| 板端网关 | `0.0.0.0`，同网段直连无需网关 |
| TCP 接收端口 | `9000` |

Windows 按 `Win+R`，运行 `ncpa.cpl`，打开实际接网线的适配器属性，在 IPv4 属性中设置地址和掩码。独立直连时网关、DNS 可留空。若使用已有局域网，应按实际网段分配不同且未被占用的地址。

板端配置位于 [photo_ethernet_config.h](XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_ethernet_config.h)：

```c
#define PHOTO_ETH_IP       {192, 168, 50, 2}
#define PHOTO_ETH_NETMASK  {255, 255, 255, 0}
#define PHOTO_ETH_GATEWAY  {0, 0, 0, 0}
#define PHOTO_ETH_PC       {192, 168, 50, 1}
#define PHOTO_ETH_PORT 9000
```

`PHOTO_ETH_PC` 必须填写**实际运行接收程序的电脑有线网卡地址**。修改头文件后需重新编译烧录；程序每次传图都会应用这份网络配置。多块板需要分配不同的 IP 和 `PHOTO_ETH_MAC`。

沿用板级引脚：SPI1 CLK=9、D0=11、D1=10、SS0=12，W5500 RST=13、INT=14。图像程序独占 W5500 socket 2 和网络配置，不要同时运行网口测试、iperf 或占用同一 SPI1 设备的示例。

## 固件配置、编译与烧录

在原来已调通摄像头和 YOLO 的构建环境中更新源码，保留现有摄像头、KPU、LCD、shell、POSIX 和互斥量配置。Linux 区分目录名大小写：本仓库为 `App_Framework`，已有虚拟机工程可能为 `APP_Framework`，同步时沿用目标目录名称。

有线传输相关文件：

```text
XiUOS/
├── App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/
│   ├── Kconfig
│   ├── photo_bridge.h
│   ├── photo_ethernet.h
│   └── photo_ethernet_config.h
└── Ubiquitous/XiZi_IIoT/board/edu-riscv64/third_party_driver/ethernet/
    └── connect_w5500.c
```

先备份编译目录的 `.config`、`xsconfig.h` 和板级 `board/edu-riscv64/.config`。**menuconfig 会先从板级 `.config` 复制配置**；如果已验证的配置仅在编译目录中，备份后先同步到板级配置，避免被默认值覆盖。

进入 `XiUOS/Ubiquitous/XiZi_IIoT`，执行：

```sh
make BOARD=edu-riscv64 menuconfig
```

按 `/` 搜索配置名，确认以下设置并保存：

```text
CONFIG_BSP_USING_UART=y
CONFIG_BSP_USING_UART_HS=y
CONFIG_BSP_USING_SPI=y
CONFIG_BSP_USING_SPI1=y
CONFIG_BSP_SPI1_USING_SS0=y
CONFIG_BSP_USING_WIZCHIP=y
CONFIG_BSP_USING_W5500=y
CONFIG_BSP_USING_LCD=y
CONFIG_USING_K210_YOLOV2_DETECT=y
CONFIG_K210_MODEL_BUILTIN=y
CONFIG_PHOTO_ETHERNET_TCP=y
CONFIG_PHOTO_FACE_CONFIRM_FRAMES=5
CONFIG_PHOTO_FACE_ABSENT_FRAMES=10
# CONFIG_PHOTO_WIFI_TCP is not set
# CONFIG_MOUNT_USB is not set
# CONFIG_SEPARATE_COMPILE is not set
```

上述为关键配置，不是可覆盖整个工程的完整 `.config`。依赖不满足而隐藏的关闭项，可能不会写入文件。连续确认和无脸恢复帧数可设为 1–120。

- **控制台必须保留**：`edu-riscv64 feature → Using UART device → Enable High Speed UART`。三个名称保持默认 `uart0`、`uart0_drv`、`uart0_dev0`，用于日志和命令交互。
- **模型使用内置版本**：无需挂载 U 盘。如果 `mount usb` 显示 `-*-`，先在 `Using CH376 device` 下关闭 `Using CH376 USB`，再到 `config default board resources → config board peripheral` 取消 `mount usb`。
- **通过 menuconfig 保存退出**：它会同步生成编译配置头，不要只手改 `.config`。

编译：

```sh
make BOARD=edu-riscv64
```

成功后使用 K-Flash 烧录本次生成的固件。修改源码或配置需要重新烧录；日常启动只需上电或复位。

## 启动电脑接收端

实际接收电脑需要 Python 3.10 或更新版本。双击 [Start-Ethernet-Photo.cmd](Start-Ethernet-Photo.cmd)，脚本优先使用项目 `.venv`，否则调用系统 Python。接收程序无需额外 Python 包。

也可在项目根目录运行：

```powershell
python -u pc_bridge/tcp_bridge.py --port 9000
```

看到以下输出后，再让开发板触发采集：

```text
[监听 TCP] 0.0.0.0:9000
```

保持窗口打开，按 `Ctrl+C` 停止。图片默认写入接收程序所在项目的 `tmp_photos/`，也可指定 `--output`。部署接收端时保留 `Start-Ethernet-Photo.cmd` 和 `pc_bridge/` 的相对位置；`tcp_bridge.py` 依赖同目录 `bridge.py` 中的图像转换函数。

若 Windows 防火墙阻止连接，可在**接收电脑的管理员 PowerShell**中添加规则。以下地址对应默认配置，使用其他地址时同步修改：

```powershell
New-NetFirewallRule -DisplayName "XiUOS Photo TCP 9000" `
  -Direction Inbound -Action Allow -Protocol TCP `
  -LocalAddress 192.168.50.1 -LocalPort 9000 `
  -RemoteAddress 192.168.50.2 -Profile Any
```

## 查看板端日志与检查连接

串口用于调试日志和命令，可与网线接收程序同时使用。[Open-Board-Terminal.cmd](Open-Board-Terminal.cmd) 默认连接 COM3，115200 波特率、8N1；串口号不同则修改脚本。首次启动该终端脚本可能需要联网安装 pySerial。烧录时关闭占用同一串口的终端，烧录后重新打开并按 RESET 查看启动日志。

启动后应出现：

```text
[photo] confirm=5 consecutive frames, rearm=10 absent frames
[photo-ethernet] W5500 TCP enabled
```

达到连续检测条件后，诊断输出包括：

```text
[photo-ethernet v2] id=1 chip=0x04 ... link=UP
[photo-ethernet] board=192.168.50.2 mask=255.255.255.0 gateway=0.0.0.0
[photo-ethernet] target=192.168.50.1:9000 ...
```

成功时电脑应显示保存路径，板端应打印 `[photo-ethernet] PC saved ...` 并显示 LCD 提示。`link=UP` 只代表物理链路已建立，不代表 TCP 已连接。

连接失败时，在**实际接收电脑**执行：

```powershell
ipconfig
netstat -ano | findstr :9000
ping -n 4 192.168.50.2
arp -a
```

确认有线 IP 与板端 `target` 一致，9000 端口存在 `LISTENING`。ping 和 ARP 检查应在板端至少尝试过一次上传后进行，因为程序会在尝试传图时应用配置地址。在 `192.168.50.1` 对应接口下检查板端 ARP 记录，默认板端 MAC 为 `02-58-49-55-00-01`。ping 失败需结合 ARP 和监听结果判断，不能单独归因于防火墙。

| 现象 | 检查方法 |
| --- | --- |
| `USB lab requires CH376...` 编译错误 | 内置模型模式下关闭 `Using CH376 USB` 和 `MOUNT_USB`，保存配置 |
| `SERIAL_BUS_NAME_0` 等宏未定义 | 恢复 `BSP_USING_UART` 和 `BSP_USING_UART_HS`，保存配置 |
| 接收窗口没有监听提示 | 用命令行直接启动 `tcp_bridge.py`，查看 Python 或端口占用错误 |
| `chip` 不是 `0x04` | 检查 W5500 SPI、供电和复位 |
| `link=DOWN` | 检查网线、RJ45 接口及电脑网口是否启用 |
| `failed stage=connect` | 核对目标 IP、端口监听、ARP 和防火墙，保留完整 `reason/SR/IR` 日志 |
| `rc=0`、`SR=0x13` 后超时 | 非阻塞接口仍在处理，socket 停在 INIT，不表示连接成功 |
| 检测画面暂时停顿 | 连接和传输在检测线程中等待；应用层连接上限 10 秒、发送 30 秒、保存确认 10 秒 |
| 有人脸但不触发 | 检查是否连续满足 5 帧；已尝试过则先连续 10 帧无脸，再重新进入 |
| PNG 已保存但没有成功提示 | 检查保存确认是否到达板端以及 LCD 配置，保存本身不等于板端已收到确认 |

底层驱动仍有硬件寄存器轮询，SPI/芯片异常需另行排查，不能用上述应用层超时保证覆盖所有硬件故障。

## 验证状态

- 已通过有线固件头文件的主机 C 测试：连续帧确认、计数重置、再次触发、短写、分片 ACK、错误 ID/CRC、超时、断连、芯片版本和链路检查。
- 已通过 C 数据包到 Python TCP 接收器的 PNG 保存及 ACK 联动测试，以及接收端相关回归测试。
- 实板已运行人脸检测和有线诊断代码，最新日志显示 `chip=0x04`、`link=UP`，但仍在 TCP 连接阶段超时；**实板图片上传、LCD 成功提示和实际速率尚未验收**。

主机测试不能代替实板验收。可在项目根目录运行以下测试，GCC 路径按实际安装位置修改：

```powershell
python pc_bridge/test_ethernet_firmware.py "D:/Software Install/CodeBlocks/MinGW/bin/gcc.exe"
python -m unittest discover -s pc_bridge -p "test_tcp_bridge.py" -v
```

## 主要文件

| 路径 | 用途 |
| --- | --- |
| [Start-Ethernet-Photo.cmd](Start-Ethernet-Photo.cmd) | 启动电脑 TCP 图片接收端 |
| [Open-Board-Terminal.cmd](Open-Board-Terminal.cmd) | 打开开发板调试终端 |
| [pc_bridge/tcp_bridge.py](pc_bridge/tcp_bridge.py) | TCP 接收、校验、保存和确认 |
| [photo_ethernet.h](XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_ethernet.h) | 板端有线传输与连接诊断 |
| [photo_bridge.h](XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_bridge.h) | 连续帧触发及 LCD 保存提示 |
| `tmp_photos/` | 接收的 PNG 照片 |
| `output/` | 替换包和本地产物 |
