# XiUOS 人像采集串口接口

新增无线采集使用 `tcp_bridge.py`，根目录双击 `Start-WiFi-Photo.cmd` 即可启动。板端配置、虚拟机 menuconfig 与编译步骤见 [WIFI_SETUP.md](WIFI_SETUP.md)。以下内容为原串口模式。

本接口配合本仓库 `XiZi_IIoT` 的 **edu-riscv64 / K210** 固件使用。默认 PC 串口为 COM3，115200、8N1，无流控。只运行 PC 程序不能从旧固件的检测坐标还原照片，必须先编译并烧录下面的固件修改。

## 依据和实现

- `docs/06_矽璓操作系统实验-RISCV篇_20230829.docx` 第 5.5 节：`k210_detect`、摄像头采集、KPU 推断及 LCD 显示流程。
- `docs/04_矽璓操作系统原理_20230828.pdf` 的线程同步、互斥量及设备访问说明。
- 具体 API 和像素布局以当前仓库 `transform.h`、`connect_dvp.c`、`connect_lcd.c` 和 `k210_yolov2_detect.c` 核对；当前 `USB/FACE.JSN` 的图像为 320×240。

流程：PC 收到 `detect_info.obj_number:大于0` → 发送 `photo_capture ID` → 检测线程在下一次处理到请求时发送完整摄像头帧（画检测框之前）→ PC 校验数据、写临时文件、flush/fsync、原子改名为 PNG → 发送 `photo_saved ID` → 同一个检测线程在 LCD 最底部显示“人像已完成采集”，等待 2000ms 后清除。

使用已有的摄像头采集线程，避免两个线程争抢 DVP。获取的是触发后处理请求时的一帧，不保证与触发日志属于同一帧。串口请求由原有 shell 解析，没有另建串口读取线程。

## 开发板安装

修改集中在目录：

`XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/`

- `k210_yolov2_detect.c`：增加发送和显示挂接点，循环短暂让出 CPU 使 shell 能处理请求。
- `photo_bridge.h`：串口命令、分块传输、互斥保护的确认状态和 LCD 弹窗。
- `photo_toast_font.h`：中文提示位图，无需在板上安装中文字库。

将以上三个文件同步到**你已成功编译 YOLO 的开发环境**。保留已经调通的配置，在 `XiUOS/Ubiquitous/XiZi_IIoT` 下执行：

```sh
make BOARD=edu-riscv64
```

使用既有 K-Flash 流程烧录生成的固件。启用 `ADD_XIZI_FEATURES`、`BSP_USING_LCD`、`TOOL_SHELL`、mutex/POSIX 支持及原有 YOLO/摄像头/KPU 配置。当前仓库的板级默认 `.config` 未启用 LCD，**不要用它覆盖已调通的配置**。本桥接代码仅为 XiZi + LCD 构建启用。

启动后沿用现有 U 盘模型加载流程（`/FACE.JSN` 与 `/FACE.KMD`）。shell 命令列表应有 `photo_capture` 和 `photo_saved`。若自动模型加载期间已出现检测日志，直接启动 PC 接口即可。

## PC 启动

先关闭其他占用 COM3 的串口终端；Windows 上同一串口由本程序独占。本程序会继续打印检测日志。

在项目根目录执行（Python 3.10+）：

```powershell
python -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r pc_bridge/requirements.txt
.\.venv\Scripts\python.exe pc_bridge/bridge.py --port COM3 --baud 115200
```

照片默认保存到本项目 `tmp_photos/portrait_日期_时间_请求ID_随机后缀.png`。默认目录由脚本位置确定，不受启动时工作目录影响。按 Ctrl+C 停止。串口被占用或断开会明确报错，处理连接后重新启动；程序不会自动烧录或重启开发板。

默认持续有人时只采集一次；连续 10 个检测帧无目标后重新触发。可用 `--absent-frames 3` 改成 3 帧。此处仅按检测数量触发，适用于当前 face 模型，不是身份识别，也不区分画面中的不同人。

参数：`--output 路径`、`--timeout 90`（一次请求总时限，秒）、`--no-flip-y`（取消与现有 LCD 行序一致的上下翻转）、`--swap-pairs`（交换相邻像素）、`--bgr`（交换红蓝）。默认按小端 RGB565 解码；需要用实拍和彩色物体验证当前硬件的方向和通道，按实际结果调整参数。

## 协议及限制

PC 命令以 CR 结尾，ID 为 1～2147483647 的十进制整数。开发板输出为 ASCII 文本，适合与原有日志共存：

```text
@PHOTO BEGIN ID WIDTH HEIGHT BYTES RGB565LE CRC32_HEX
@PHOTO DATA ID BYTE_OFFSET HEX_DATA
@PHOTO END ID
```

每块最多 64 字节；CRC32 为 CRC-32/ISO-HDLC。PC 检查请求 ID、尺寸、长度、顺序、CRC32；错误、超时或磁盘写入失败均不会发送 `photo_saved`。每次保存使用唯一文件名。开发板仅接受正在等待保存的相同 ID，忽略过期和重复确认。发送中不接受新采集；等待确认时允许后续新请求替代旧请求。

320×240 RGB565 原图为 153600 字节。加上十六进制编码及记录头，115200 波特率理论传输约 34 秒，实际还受调度影响，默认总超时 90 秒。传图期间暂停下一帧采集，弹窗显示期间停留 2 秒。照片写好后才会弹窗；断线或 ACK 丢失可能导致照片已保存但没有弹窗，此时不会伪造成功提示。传输期间应避免其他任务大量输出串口日志；行内被日志打断会校验失败，不会保存损坏照片。

失败后不会无限重试：需连续无目标后重新进入画面，或处理故障后重启 PC 接口。下一次请求可恢复开发板等待确认的状态。PC 超时应长于整张传输时间；若在传输中重启 PC，请先等待旧传输结束。

## 验证

```powershell
python -m unittest discover -s pc_bridge -v
python pc_bridge/test_firmware.py "D:/Software Install/CodeBlocks/MinGW/bin/gcc.exe"
```

第一条覆盖碎片输入、重复/缺失块、校验错误、错误 ID、超时、保存失败、触发防抖及 PNG 像素顺序。第二条编译实际桥接头文件的主机测试，验证 ACK 状态、弹窗位置和两秒延时，并将 C 输出交给 Python 接收器保存 PNG。

主机测试不能替代 RISC-V 完整固件编译和实板测试。本机没有可用的 WSL/RISC-V 构建环境，本次未烧录固件或验证真实 COM3 图像/LCD。实板验收：有人时保存一张可打开的照片，保存后底端中文显示两秒；持续有人不重复拍照；离开足够帧后再次进入能重新拍照；断线或保存失败时不显示成功提示。
