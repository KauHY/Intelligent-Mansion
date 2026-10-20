# ESP8285 TCP 人像采集：Windows 与虚拟机操作

当前 v8 针对中途 CLOSED 采用 115200 波特率、1024 字节分块做稳定性对照，见 [v8 更新说明](TRANSFER_CLOSED_V8.md)。以下旧版提速参数不作为本次测试设置。

已有 v6 环境请先按 [v7 更新说明](PERFORMANCE_ORIENTATION.md) 替换摄像头处理和 LCD 驱动文件，并设置传输速率。以下保留首次配置步骤。

引脚方向已按同板 `Nuttx_Fusion_XiUOS/aiit_board/edu-riscv64/src/k210_bringup.c` 交叉映射核对：IO6 是 ESP TX，接 K210 RX；IO7 是 ESP RX，接 K210 TX。旧包使用者先阅读 [PIN_FIX.md](PIN_FIX.md)。

本版本新增可选 `PHOTO_WIFI_TCP`，保留已有 CPU0 启动修复。默认不开启，以免改变现有串口固件行为。完成以下配置后必须重新编译、烧录一次；以后正常启动无需再次烧录。

流程：检测到目标 → 使用当前摄像头帧（尚未画检测框）→ ESP8285 TCP 二进制上传 → Windows 校验 CRC32、保存 PNG → 回传对应图片 ID 和 CRC → LCD 底部显示“人像已完成采集”两秒。

当前模型应为 FACE 模型：以 `detect_info.obj_number > 0` 触发。保持有人只采集一次，连续 10 帧无人后重新允许触发；失败也需无人后重新进入画面。联网、传图和两秒提示均在原摄像头线程中完成，期间检测画面暂停，避免读写同一帧和多个线程操作 LCD。

## 1. Windows：先启动接收端

1. Windows 与开发板使用同一局域网；ESP8285 使用 **2.4 GHz Wi-Fi**。避免开启设备隔离的访客网络。
2. Windows 终端执行 `ipconfig`，记录当前联网网卡的 IPv4，例如 `192.168.1.100`。不要填 `127.0.0.1`、`0.0.0.0`、VMnet/NAT 虚拟网卡地址。
3. 双击根目录 **`Start-WiFi-Photo.cmd`**，看到 `[监听 TCP] 0.0.0.0:9000`。需要 Python 3.10+，不需要额外 Python 包；优先复用项目 `.venv`。
4. Windows 防火墙首次询问时允许 Python 在可信的专用网络通信。若连接仍被拦截，可在**管理员 PowerShell**为专用网络添加规则：

```powershell
New-NetFirewallRule -DisplayName "XiUOS Photo TCP 9000" -Direction Inbound -Action Allow -Protocol TCP -LocalPort 9000 -Profile Private -RemoteAddress LocalSubnet
```

接收目录固定基于脚本所在项目根目录解析，默认是 `IntelligentMansion/tmp_photos`，与双击时的工作目录无关。可以同时用 `Open-Board-Terminal.cmd` 查看 COM3 日志。Wi-Fi 模式不运行旧的 `bridge.py`。

命令行等价用法：

```powershell
python pc_bridge/tcp_bridge.py --port 9000
```

本接收器按单开发板依次处理连接。协议用于可信局域网，没有 TLS 或身份认证；不要将 9000 端口映射到公网。CRC32 用于传输完整性校验。

## 2. 虚拟机：同步文件和填写网络配置

虚拟机只负责编译时，网络使用 NAT 即可。接收端运行在 Windows，板端始终填写 Windows 的局域网 IP。只有把接收器也移到虚拟机时，才需要给虚拟机配置桥接网络，并填写虚拟机实际局域网 IP、开放它的防火墙端口。

将本项目下列文件同步到虚拟机中**已调通 YOLO 的同一个 XiUOS 源码树**：

```text
App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/
  Kconfig
  k210_yolov2_detect.c
  photo_bridge.h
  photo_wifi.h
  photo_wifi_config.example.h
  photo_toast_font.h
```

Linux 区分大小写；原构建树可能将应用目录命名为 `APP_Framework`。沿用该树已有目录名，放入现有检测目录，不要另外创建一个同名但大小写不同的目录。此次没有修改内核、UART 驱动或已修复的 CPU0 绑定逻辑。

在虚拟机终端进入该检测目录，执行：

```sh
cp -n photo_wifi_config.example.h photo_wifi_config.h
nano photo_wifi_config.h
```

修改这四项，例如：

```c
#define PHOTO_WIFI_SSID "MyWiFi_2G"
#define PHOTO_WIFI_PASSWORD "实际密码"
#define PHOTO_WIFI_PC "192.168.1.100"
#define PHOTO_WIFI_PORT 9000
```

v7 设置：`PHOTO_WIFI_BOOT_BAUD=115200`、`PHOTO_WIFI_BAUD=460800`、`PHOTO_WIFI_ENABLE_PIN=8`、`PHOTO_WIFI_ABSENT_FRAMES=10`。名称或密码中的双引号、反斜杠需按 C 字符串写成 `\"`、`\\`；不要加入换行。

`photo_wifi_config.h` 已被 Git 忽略，但必须存在于实际编译树。首次不要使用示例 Wi-Fi 名称直接烧录。密码会编入固件，不要公开自己的配置头和固件。

## 3. 虚拟机：menuconfig

进入已有构建目录：

```sh
cd /你的实际路径/XiUOS/Ubiquitous/XiZi_IIoT
```

**先备份当前已经验证能运行的配置。** 本仓库 `make menuconfig` 会先把 `board/edu-riscv64/.config` 复制到根 `.config`，因此先同步当前有效配置到板级目录，避免旧默认配置覆盖它：

```sh
cp .config .config.before-photo-wifi
cp board/edu-riscv64/.config board/edu-riscv64/.config.before-photo-wifi
cp .config board/edu-riscv64/.config
make BOARD=edu-riscv64 menuconfig
```

如果你原来的有效配置只保存在板级目录，先确认并将其复制到根 `.config`，再执行上述操作。不要使用默认配置替代已调通配置，不要执行 `distclean`。

在 menuconfig 中按 `/` 搜索下面的符号，再按搜索结果提示进入相应菜单；空格切换、Enter 修改、保存退出。

| 配置符号 | 设置 | 用途 |
|---|---|---|
| `BSP_USING_UART1` | `y` | ESP8285 独占 UART1 |
| `BSP_UART1_TXD_PIN` | `7` | 板载 Wi-Fi UART 引脚，默认 19 需修改 |
| `BSP_UART1_RXD_PIN` | `6` | 板载 Wi-Fi UART 引脚，默认 18 需修改 |
| `SERIAL_1_DEVICE_NAME_0` | `uart1_dev1` | 程序打开 `/dev/uart1_dev1` |
| `BSP_USING_GPIO` | `y` | 控制 IO8 Wi-Fi EN |
| `BSP_USING_LCD` | `y` | 显示保存成功提示 |
| `ADAPTER_ESP8285_WIFI` | `n` | 避免已有 Wi-Fi 适配器同时读写 UART1 |
| `SEPARATE_COMPILE` | `n` | 当前功能使用整体编译下的毫秒计时接口 |
| `USING_K210_YOLOV2_DETECT` | `y` | 保留现有 YOLO 功能 |
| `PHOTO_WIFI_TCP` | `y` | 新增的 TCP 图片上传功能 |

其余摄像头、KPU、LCD、POSIX、互斥量、U 盘和 CPU 配置沿用已经验证的版本。新选项在 K210 YOLOv2 检测配置附近，依赖项满足后才可见。启用它需要 XiZi、edu-riscv64 板型。

COM3 使用的高速调试串口仍为 115200；U 盘使用的 UART3 保持原配置。不要运行 UART1/Wi-Fi 测试命令，不要让其他外设占用 IO6、IO7、IO8。GPIO 设备名沿用默认 `pin_dev`。

编译前可以检查：

```sh
grep -E 'CONFIG_(PHOTO_WIFI_TCP|BSP_USING_UART1|BSP_UART1_TXD_PIN|BSP_UART1_RXD_PIN|BSP_USING_GPIO|BSP_USING_LCD|ADAPTER_ESP8285_WIFI|SEPARATE_COMPILE)' .config
```

应看到 `CONFIG_PHOTO_WIFI_TCP=y`、TX=7、RX=6 等；被关闭的选项也可能以 `# CONFIG_... is not set` 表示。

## 4. 编译、烧录、验收

在 `XiZi_IIoT` 目录执行：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
find build -type f \( -name '*.bin' -o -name '*.elf' \)
```

`clean` 清除旧目标文件，保留配置；这可避免新增头文件没有被增量构建识别。使用本次生成的 **BIN** 在 Windows KFlash 烧录；同次 **ELF** 保留用于定位故障，它不是 BIN。不要再次选择旧版本 BIN。

保留原 U 盘模型，先启动 Windows 接收器，再复位开发板。COM3 应显示：

```text
[photo-wifi] enabled; awaiting face; UART1 dedicated to ESP8285
...检测到人脸后...
[photo-wifi] initializing ESP8285
[photo-wifi] joining AP
[photo-wifi] connecting PC 192.168.1.100:9000
[photo-wifi] sending id=1 bytes=153600 crc=...
[photo-wifi] PC saved id=1
[photo] saved toast complete
```

Windows 对应显示 `[接收]`、`[保存成功]` 和实际 PNG 路径，随后 LCD 提示两秒。PC 必须完成 CRC 校验、PNG 写入、flush/fsync 和原子改名后才会发送确认。若文件已保存但确认丢失，文件保留，板端不会显示成功；这属于“保存完成但未确认”，不要当成图片丢失。

| 停在哪一步 | 排查 |
|---|---|
| 没有 `enabled` | 仍是旧固件，或 `PHOTO_WIFI_TCP` 未启用 |
| `initializing` 后失败 | UART1 引脚/设备名、IO8、模块上电 AT 波特率、ESP8285 是否运行支持这些命令的 AT 固件 |
| `joining AP` 后失败 | SSID/密码、2.4 GHz、信号、模块 AT 版本；连接允许 30 秒 |
| `connecting PC` 后失败 | PC IP、TCP 9000 监听、防火墙、访客网络隔离；连接允许 10 秒 |
| `sending` 后失败 | PC CRC/保存错误、UART 丢字节、断线、确认未返回 |
| 图片上下颠倒 | PC 启动加 `--no-flip-y` |
| 相邻像素错位或红蓝颠倒 | 根据实图分别测试 `--swap-pairs` 或 `--bgr` |

v6 板端发送阶段默认上限 300 秒，在每块开始前检查；已开始的块可能额外等待提示符 5 秒和 SEND OK 15 秒及 UART 写入时间。PC 整图接收上限默认 360 秒，单次无新数据等待最多 30 秒，板端等待保存确认最多 10 秒。可用 PHOTO_WIFI_FRAME_TIMEOUT_MS 覆盖板端阶段时限（毫秒）。失败不提示成功，不循环重发；连续 10 帧无人后可重试。下一次尝试会在必要时通过 EN 复位模块，恢复 AT 命令状态，不执行恢复出厂设置。

## 5. 跑通后提速

320×240 RGB565 原始数据为 153600 字节。8N1 下，不含 AT/TCP 开销的串口传输时间：115200 约 **13.3 秒**，921600 约 **1.67 秒**。实际还需联网、分块响应、PC 存储和两秒提示；Wi-Fi 本身不能消除 K210→ESP8285 的 UART 瓶颈。

v7 示例使用 `PHOTO_WIFI_BAUD=460800`，已有私有头文件须手动修改。保留 `PHOTO_WIFI_BOOT_BAUD=115200`，除非你已确认模块持久化上电波特率不是它。先验证 460800 的成功率，不要直接跳到 921600。

程序先以 BOOT_BAUD 联系模块，发送 `AT+UART_CUR` 后再修改 K210 UART。该更改不写入模块持久化波特率；COM3 和 KFlash 波特率不随之改变。高速不稳定时回退 460800 或 115200。最终速度和模块 AT 固件兼容性必须实板确认。

## 协议与验证范围

- 每张图片一个 TCP 连接，端口默认 9000；普通 `AT+CIPSEND` 定长发送，v7 每块默认最多 2048 字节，UART 每 64 字节间隔 2ms，不使用透传模式。
- 帧头 24 字节，网络字节序：`XPH1`、ID(uint32)、宽(uint16)、高(uint16)、字节数(uint32)、CRC32(uint32)、格式(uint32，1=RGB565LE)。像素负载保持 RGB565 **小端**，CRC 为标准 CRC32/ISO-HDLC，仅覆盖像素。
- 成功确认 12 字节：`XPA1`、同一 ID(uint32)、同一 CRC32(uint32)，整数为网络字节序。失败关闭连接，不发成功确认。
- PC 默认按现有 LCD 帧缓冲方向翻转行序保存 PNG。图片按原始分辨率保存，不含检测框。
- 主机测试覆盖真实 C 头文件的 AT/IPD 分片、短写、全尺寸二进制数据、正确/错误/缺失确认、重复触发抑制和两秒 LCD；以及 Python 的断流、超时、CRC、文件保存失败。主机测试不替代 RISC-V 完整编译或实板网络测试。

板载接线依据项目 `/docs` 的 06 教学实验指导书（IO6/IO7 Wi-Fi UART、IO8 Wi-Fi EN）；系统配置方法结合 04 操作系统原理及本仓库实际 Kconfig/Makefile。AT 定长发送和 `+IPD` 格式参考 [Espressif ESP8266 AT TCP/IP 文档](https://docs.espressif.com/projects/esp-at/en/release-v2.2.0.0_esp8266/AT_Command_Set/TCP-IP_AT_Commands.html)。模块固件需支持 AT、ATE0、CWMODE、CWJAP、CIPMUX、CIPMODE、CIPDINFO、CIPSTART、CIPSEND、CIPCLOSE；改波特率另需 UART_CUR。
