# v7：减少发送等待、入网重试与安装方向修正

当前修订：按用户要求恢复旧版 LCD 扫描方向，其他 v7 改动保持不变。已安装 v7 的虚拟机只需覆盖包内 `board/edu-riscv64/third_party_driver/lcd/connect_lcd.c` 到同路径，然后 clean、编译并烧录；无需替换其他源码或更改 PC 接收程序。

本次针对实际五次仅成功一次、传输超过一分钟、摄像头和 LCD 安装方向倒置的问题。它是待实板验收的源码更新，不表示成功率或耗时已达标。

## 改动与边界

- 保留完整 320×240 RGB565 图片、CRC 和保存后确认。每块从 256 增至 2048 字节，一张图从 601 次 AT 发送请求降到 76 次（均含图片头）。模块仍按 64 字节 UART 小段、2ms 间隔接收，避免直接连续灌入大块。
- 示例配置将传图 UART 速率改为 460800，启动探测仍为 115200。旧私有配置不会自动覆盖，必须修改 PHOTO_WIFI_BAUD。这与 COM3 终端和 KFlash 波特率无关。
- 入网收到明确的超时/找不到 AP/连接失败终止回复后，最多尝试三次，间隔 1、2 秒。密码错误直接停止；命令无终止回复时不向仍忙碌的模块追加命令。无法保证此举解决路由器 DHCP、信号或供电问题。
- 摄像头完成 DVP 后、KPU 推理之前，将显示 RGB565 和三路平面 RGB 同时旋转 180°。保留像素内字节顺序、颜色通道和模型填充区。识别框随后在修正后的坐标中生成。PC 不再额外旋转，原有 LCD 行序转换保留。
- LCD 驱动已恢复旧版 `DrvLcdSetDirection(DIR_YX_RLUD)`（0x20），移除 v7 新增的 LCD 旋转开关。成功提示保持原有绘制逻辑和两秒时长。摄像头数据的旋转修正继续保留。
- 两端成功日志增加耗时统计。板端 transfer_ms 从 TCP 建立后计算，PC 从开始读取图片头到落盘计算；两者不包括完整入网时间。

理论上，153600 字节在 115200、8N1 下仅串口就需约 13.3 秒；460800 下约 3.3 秒，另有约 4.8 秒小段间隔及 AT/网络/保存开销。不能把 3.3 秒当成端到端承诺。若目标是稳定 1–2 秒，还需要 JPEG/降低分辨率或更换传输链路，本次没有以降低清晰度换取速度。

AT 分块最大 2048 字节依据：
https://docs.espressif.com/projects/esp-at/en/release-v2.3.0.0_esp8266/AT_Command_Set/TCP-IP_AT_Commands.html#at-cipsend

## 虚拟机替换和编译

解压 `XiUOS-WiFi-Builtin-VM-v7-lcd-rollback.zip`。若已安装 v7，只替换下表 LCD 驱动；若从 v6 升级，替换/新增下表全部文件：

| 包内文件 | 虚拟机位置 |
|---|---|
| detector/photo_wifi.h | 原检测目录同名文件 |
| detector/k210_yolov2_detect.c | 原检测目录同名文件 |
| detector/photo_orientation.h | 原检测目录新增文件 |
| board/edu-riscv64/third_party_driver/lcd/connect_lcd.c | XiZi_IIoT/board/edu-riscv64/third_party_driver/lcd/connect_lcd.c |

原检测目录为 `App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure`。若实际为 `APP_Framework`，沿用实际路径，不能新建一个大小写不同的副本。模型、main.c、本地网络参数及菜单配置无需重置。

在虚拟机的原 `photo_wifi_config.h` 中，仅修改/确认：

```c
#define PHOTO_WIFI_BOOT_BAUD 115200
#define PHOTO_WIFI_BAUD 460800
/* 如果旧文件已自定义 PHOTO_WIFI_BLOCK_BYTES，把它改为 2048。 */
```

SSID、密码、PC 地址保持当前有效值，UART1 TX=7、RX=6。不要用示例文件覆盖私有配置。如果 460800 时握手/传输失败，可回退 PHOTO_WIFI_BAUD=115200 对比；必要时将分块回退为 1024，而不是继续放大超时。

在 `~/test/xiuos/Ubiquitous/XiZi_IIoT` 执行：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

烧录这次生成的 BIN。期待传输日志出现 `[photo-wifi v7] block=2048`，成功日志出现 `baud=460800`。不需要改 COM3 终端波特率。

Windows 的 tcp_bridge.py 已更新统计输出。退出旧接收窗口，重新启动 Start-WiFi-Photo.cmd。另一台 PC 可复制包内 Windows/tcp_bridge.py，保留已有 bridge.py。

## 方向与成功率验收

按设备最终安装方向固定板子，使用带明显上下左右标记的画面检查摄像头识别与 PC 图片方向。LCD 扫描及文字绘制已按要求恢复旧版行为；预览使用的摄像头帧仍保留 v7 的数据旋转。

摄像头修正开关仍为 photo_orientation.h 中的 PHOTO_CAMERA_ROTATE_180=1。本次没有更改该值，LCD 已不再使用 PHOTO_LCD_ROTATE_180 开关。

连续测试至少 10 次，每次离开画面达到 10 个无人帧再进入，记录成功数、入网失败次数、实际波特率及 transfer_ms。如果失败，提供完整 stage/block/module_received 日志；没有实板日志不能将五次只成功一次归因于某个特定原因。当前仍在检测线程同步发送，传输期间检测会暂停；本次没有实现异步摄像和传输。

## 本机验证

已通过真实头文件的 C 主机模拟：不对称图像的 RGB565/三通道旋转及填充区保护、2048 字节分块完整性和 76 次请求计数、两次入网失败后第三次成功、密码错误不重试、UART 切换、短写/分片、超时与保存确认。C 生成的完整帧已由 Python 接收并保存，16 项 PC 回归测试通过。没有在本机进行 RISC-V 固件交叉编译或真实无线速度/成功率测试，物理安装方向也需实板验收。
