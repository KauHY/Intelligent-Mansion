# IntelligentMansion 智能楼宇人像采集项目

本项目基于搭载 XiUOS / XiZi 的 RISC-V 开发板，使用 K210 运行 YOLOv2 人脸检测模型，支持串口或板载 ESP8285 TCP 上传照片至 Windows PC，并在保存确认后显示 LCD 提示。

## ESP8285 Wi-Fi 图片采集（新增）

按 [Wi-Fi 配置与虚拟机编译步骤](pc_bridge/WIFI_SETUP.md) 启用 `PHOTO_WIFI_TCP`、设置 UART1 TX=6/RX=7，并填写本地 `photo_wifi_config.h` 的 Wi-Fi 和 PC 地址，重新编译烧录。Windows 双击 **`Start-WiFi-Photo.cmd`** 启动接收端；照片保存在本项目 `tmp_photos/`。COM3 只需用于查看调试日志，Wi-Fi 模式无需运行串口 `bridge.py`。

新增功能已提供主机协议测试；RISC-V 完整编译及 ESP8285 实板联调仍需在你的环境完成。下文保留原串口模式的使用说明。

当前已完成开发板调试及 U 盘模型加载、运行。PC 人像采集接口和配套固件修改已实现并通过主机测试，完整固件编译及新增采集功能的实板联调尚待验证。

## 快速打开开发板终端

**查看开发板输出、输入命令不需要重新烧录。** 固件已写入 Flash 后，正常上电即可运行；只有需要更新固件时才需使用 K-Flash 烧录。

1. 连接开发板并供电，在 Windows 设备管理器中确认串口为 `Prolific USB-to-Serial Comm Port (COM3)`。
2. 关闭 K-Flash、人像采集程序和其他占用 COM3 的串口终端。
3. 双击项目根目录的 **`Open-Board-Terminal.cmd`**。

启动脚本使用以下连接参数：

| 参数 | 默认值 |
| --- | --- |
| 串口 | COM3 |
| 波特率 | 115200 |
| 数据位 / 校验 / 停止位 | 8 / 无 / 1 |
| 流控 | 无 |
| 命令行结束符 | CR |

PC 需安装 Python 3.10 或更新版本，并可通过 `python` 或 `py -3` 调用。首次启动时，脚本会在项目内创建 `.venv`，自动安装 pySerial，需要联网。后续启动会复用该环境。

- 连接成功但没有提示符：先按一次回车。
- 想看完整启动日志：保持终端打开，按开发板 **RESET** 复位键。终端不会补发连接前的日志。
- 退出终端：按 **Ctrl + ]**，再按任意键关闭脚本窗口。
- 如果系统分配了其他 COM 端口：修改 `Open-Board-Terminal.cmd` 中实际执行 `serial.tools.miniterm` 命令的 `COM3`，并同步修改提示文字。

## 人像采集

### 开发板准备

旧固件的 `detect_info.obj_number` 日志仅包含检测结果，没有图片数据。使用人像采集接口前，需要将配套固件修改编译、烧录到开发板。

修改文件位于：

```text
XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/
├── k210_yolov2_detect.c
├── photo_bridge.h
└── photo_toast_font.h
```

请在已经调通 YOLO 的构建环境中，保留原有配置并同步上述文件，在 `XiUOS/Ubiquitous/XiZi_IIoT` 目录执行：

```sh
make BOARD=edu-riscv64
```

需要启用原有摄像头、KPU、U 盘、YOLO 配置，以及 LCD、shell、互斥量和 POSIX 支持。仓库板级默认 `.config` 未启用 LCD，不要用它覆盖已经验证可用的配置。

模型文件位于 `USB/`。沿用已调通的 U 盘挂载方式，将模型与配置文件放在 U 盘根目录，确保开发板可通过 `/FACE.KMD` 和 `/FACE.JSN` 访问。当前应用在启用 `USING_K210_YOLOV2_DETECT` 和 `MOUNT_USB` 时会调用 `k210_detect("/FACE.JSN")`。

`bins/` 保存了多个历史版本的固件；不能仅凭文件名版本号判断某个 BIN 是否包含本次采集功能，应以对应构建记录和实板结果为准。

### 启动 PC 接收程序

关闭普通串口终端后，在项目根目录打开 PowerShell。若已通过双击脚本成功启动过终端，可直接复用 `.venv`：

```powershell
.\.venv\Scripts\python.exe pc_bridge/bridge.py --port COM3 --baud 115200
```

如果尚未准备 Python 环境，先执行：

```powershell
python -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r pc_bridge/requirements.txt
```

接收程序自身会打印检测日志，运行时不需要再打开普通串口终端。按 **Ctrl+C** 停止接收。

### 采集流程

1. PC 收到 `detect_info.obj_number` 大于 0 的日志，向开发板发送采集请求。
2. 开发板检测线程发送处理请求时的完整摄像头帧，发送前尚未绘制检测框。
3. PC 检查数据顺序、长度和 CRC32，将图片保存为项目 `tmp_photos/` 下的 PNG。
4. 保存成功后，PC 回传对应请求的确认，开发板在 LCD 底部显示 **“人像已完成采集”**，持续 **2 秒**。

默认持续检测到目标时只采集一次，连续 10 个检测帧无目标后重新允许触发。它是人脸检测触发的拍照流程，不提供人员身份识别，也不会区分画面中的不同人。

当前模型图像尺寸为 320×240。115200 波特率下，每张照片理论传输约 34 秒，传输期间暂停下一帧采集；默认请求超时为 90 秒。传输错误或保存失败时不会发送保存成功确认。

调整无目标帧数或保存位置的示例：

```powershell
.\.venv\Scripts\python.exe pc_bridge/bridge.py --port COM3 --baud 115200 --absent-frames 3 --output ./tmp_photos
```

完整参数、协议、方向和颜色调整方法见 `pc_bridge/README.md`。

## 目录说明

| 路径 | 用途 |
| --- | --- |
| `Open-Board-Terminal.cmd` | 双击启动普通串口终端，无需烧录 |
| `pc_bridge/` | PC 接收程序、依赖声明、协议说明和主机测试 |
| `XiUOS/` | 操作系统、驱动、应用及配套固件源码 |
| `USB/` | 人脸模型文件和模型参数配置 |
| `bins/` | 历史固件 BIN 文件 |
| `K-Flash.exe` | 固件烧录工具 |
| `docs/` | 需求、操作系统原理、实验和系统设计资料 |
| `tmp_photos/` | PC 接收的照片，已配置 Git 忽略规则 |
| `output/` | 本地输出、诊断材料及历史打包文件，已配置 Git 忽略规则 |
| `tmp/` | 临时文件与测试产物，已配置 Git 忽略规则 |
| `.venv/` | 首次启动时创建的 Python 虚拟环境，已配置 Git 忽略规则 |

## 资料依据

开发板相关调查以以下文档为准：

- `docs/04_矽璓操作系统原理_20230828.pdf`：系统原理、线程同步、互斥量和设备访问。
- `docs/06_矽璓操作系统实验-RISCV篇_20230829.docx`：RISC-V 实验，尤其是第 5.5 节 YOLOv2 人脸检测。

其余项目需求和系统设计资料保存在 `docs/`，具体接口实现同时参照当前仓库源码。

## 测试与验证状态

在项目根目录运行 PC 接口测试：

```powershell
.\.venv\Scripts\python.exe -m unittest discover -s pc_bridge -v
```

有本地主机 GCC 时，可运行实际桥接头文件的 C 测试。以下为本机编译器路径，其他环境请替换为自己的 GCC 路径：

```powershell
.\.venv\Scripts\python.exe pc_bridge/test_firmware.py "D:/Software Install/CodeBlocks/MinGW/bin/gcc.exe"
```

已验证：10 项 PC 测试，以及 C 固件桥接输出 → Python 接收 → PNG 保存 → 匹配确认的主机联动测试。覆盖分包、缺失和重复数据块、CRC 错误、错误请求 ID、超时、磁盘保存失败、重复触发和弹窗延时调用。

待实板验证：完整 RISC-V 固件编译、实际 COM3 图像传输、照片颜色与方向、LCD 中文提示及实际显示时长。主机测试不代表这些硬件行为已经验收。

## 常见问题

| 现象 | 处理方法 |
| --- | --- |
| 串口提示拒绝访问或被占用 | 关闭 K-Flash、普通终端或 `bridge.py`，同一时间只保留一个 COM3 使用者 |
| 找不到 COM3 | 检查供电、USB 数据线、驱动和设备管理器中的实际端口号 |
| 双击脚本提示找不到 Python | 安装 Python 3.10+，确认 `python --version` 或 `py -3 --version` 可执行 |
| 首次依赖安装失败 | 检查联网情况及窗口中的 pip 错误，处理后再次双击脚本 |
| 终端打开后没有启动日志 | 按回车检查 shell；需要启动日志时按开发板 RESET，无需重新烧录 |
| 有检测日志但接收照片超时 | 确认固件包含 `photo_capture`、`photo_saved` 命令，检查传输时间和其他任务的串口日志干扰 |
| 一直有人但不再拍照 | 默认每次持续检测只拍一次，需连续无目标达到指定帧数后再触发 |
| 照片已保存但 LCD 未提示 | 检查确认命令是否送达、固件是否启用 LCD；断线或确认丢失可能出现这种情况 |
| 照片上下方向或颜色不正确 | 对照实物验证后，按 `pc_bridge/README.md` 调整像素方向或颜色参数 |
