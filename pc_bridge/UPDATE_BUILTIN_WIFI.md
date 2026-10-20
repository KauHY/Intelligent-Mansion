# Wi-Fi 初始化修订与内置模型更新包

v3 已纠正 UART 方向为 K210 TX=7、RX=6。已成功使用 v2 内置模型时，按 [PIN_FIX.md](PIN_FIX.md) 只更新两个文件及引脚配置即可。

本次板端日志只到 initializing，尚未连 AP，也未向 PC 发送图片。旧日志不能区分失败位置，不能据此认定是 PC 防火墙或图片格式问题。

本次改动：
- 上电等待延长至 2.5 秒；每个候选波特率尝试两次 AT 握手。
- 自动探测配置中的上电/目标波特率及 115200、921600、460800、57600、38400、19200、9600，去除重复值。
- 切换目标波特率后重新验证 AT，避免依赖模块在旧波特率返回 OK。
- 错误日志增加 stage、baud、rx，禁止输出 Wi-Fi 密码或 CWJAP 命令。
- 正常关闭连接时允许 CLOSED 通知，避免将其当成传输失败并反复重连。
- 新增 K210_MODEL_BUILTIN，模型和参数均内置；不分配第二份模型缓冲区，不释放静态模型数据。

这些修订处理启动时序和波特率差异；不能通过主机测试证明当前实板故障已消失。若模块不是 AT 固件或板端引脚/设备配置不同，仍需按新日志定位。

## 替换文件

先备份虚拟机中现有文件。本包不是完整工程，保留未列出的文件及原配置。

将 `detector/` 内全部 9 个文件复制到：

```text
~/test/xiuos/APP_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/
```

其中新增 `face_model.c`、`face_model.h`，并更新 `Makefile`。沿用你的实际应用目录名（`APP_Framework` 或 `App_Framework`），不要另建大小写不同的目录。

另将 `Applications/main.c` 更新到：

```text
~/test/xiuos/APP_Framework/Applications/main.c
```

如果该入口有你自己添加的功能，保留它们，只合并 K210_MODEL_BUILTIN 分支。它调用 `k210_detect(NULL)`，内置模式不再解析 U 盘路径。

保留虚拟机自己的 `photo_wifi_config.h`，它不在包里。首次排障建议 `PHOTO_WIFI_BOOT_BAUD` 和 `PHOTO_WIFI_BAUD` 都设为 115200；跑通后再提升目标速率。不要改 COM3 波特率。

## 虚拟机配置

在已经调通的源码树中执行：

```sh
cd ~/test/xiuos/Ubiquitous/XiZi_IIoT
cp .config .config.before-builtin-wifi
cp board/edu-riscv64/.config board/edu-riscv64/.config.before-builtin-wifi
cp .config board/edu-riscv64/.config
make BOARD=edu-riscv64 menuconfig
```

按 `/` 搜索对应符号，进入菜单设置：

```text
CONFIG_K210_MODEL_BUILTIN=y
CONFIG_PHOTO_WIFI_TCP=y
CONFIG_BSP_USING_UART1=y
CONFIG_BSP_UART1_TXD_PIN=7
CONFIG_BSP_UART1_RXD_PIN=6
CONFIG_BSP_USING_GPIO=y
CONFIG_BSP_USING_LCD=y
# CONFIG_MOUNT_USB is not set
# CONFIG_SEPARATE_COMPILE is not set
```

`ADAPTER_ESP8285_WIFI` 保持关闭。保留摄像头、KPU、YOLO、JSON 参数结构依赖、POSIX 和 CPU0 修复相关配置。若仍有其他业务使用 U 盘，可以保留 MOUNT_USB，但上电可能仍花时间等待挂载；要验证完全脱离 U 盘，请关闭它。

保存后：

```sh
grep -E 'CONFIG_(K210_MODEL_BUILTIN|PHOTO_WIFI_TCP|MOUNT_USB|BSP_UART1_TXD_PIN|BSP_UART1_RXD_PIN)' .config
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

如果链接报空间不足，保留完整报错与 map/ELF，不要通过删除内存边界检查解决。内置模型为 388776 字节的对齐 `.data`，会增加固件大小，但替代原模型堆分配；摄像头及 KPU 运行内存仍需要保留。

## 烧录与判断

烧录本次生成的 BIN，保留同次 ELF。启动 Windows 接收器，再拔掉 U 盘并复位开发板。

应首先看到：

```text
[lab5.5] builtin FACE detection; USB not required
[builtin model] FACE bytes=388776 CRC32=40429f51
```

如果仍显示 `[USB model]`，说明当前加载方式或烧录文件不对。CRC 不一致会终止模型启动，不会跳过校验。

出现人脸后，正常顺序：

```text
[photo-wifi] initializing ESP8285
[photo-wifi] probe UART1 baud=115200 TX=7 RX=6
[photo-wifi] AT ready baud=115200
[photo-wifi] joining AP
[photo-wifi] connecting PC ...
[photo-wifi] sending ...
[photo-wifi] PC saved ...
```

如果失败，保留从 initializing 到 failed 的全部新日志：

| stage | 下一步 |
|---|---|
| open UART1 / open GPIO | 核对 `/dev/uart1_dev1`、`/dev/pin_dev` 和设备配置 |
| configure EN GPIO | 核对 GPIO 启用和 IO8 是否被其他外设占用 |
| UART configuration | 核对 UART1 驱动配置 |
| AT handshake，所有候选 rx=0 | 检查 IO6/IO7、IO8 使能、模块固件和 UART 接收；尚未涉及 PC 网络 |
| AT handshake，rx>0 但无 OK | 可能存在乱码、其他固件输出或启动通知干扰，需要进一步检查模块固件 |
| disable echo / station mode / IPD format | 检查 AT 指令兼容性 |
| join AP | 检查 2.4 GHz SSID、密码与网络 |
| TCP connect | 检查 PC 地址、接收程序及防火墙 |
| PC saved ACK | 查看 PC 的校验/保存日志 |

失败后保持连续至少 10 帧无人，再让人脸进入画面才会重新触发。一次完整波特率探测可能需要数十秒，期间检测暂停。

主机验证：模型编译后字节与原 KMD 完全一致，参数与原 JSN 一致，对齐正确；Wi-Fi 测试覆盖不同上电波特率、握手重试、完整图像、早/晚/错误/缺失确认；原串口和 CPU0 启动测试通过。尚未进行此版本的 RISC-V 完整编译和实板验证。
