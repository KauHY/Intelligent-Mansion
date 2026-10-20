# v8：2048 字节块中途 CLOSED 的对照修订

最新日志确认实际波特率是 115200。三个失败块的模块接收计数均为 2048，板端在等待 SEND OK 时收到 CLOSED；PC 分别收到 4096、0、77824 字节后无新数据。这些值等于板端失败块偏移减去 24 字节头，当前失败块没有到达 PC。

这排除了“本次使用 460800”和“当前块 ESP 明确报告没有收齐”的解释，不能据此断言路由器、模块固件或 PC 哪一方首先关闭连接。PC 超时后本身也会关闭连接；原日志没有可直接关联的时间线。Recv N bytes 不等于 TCP 交付成功。

## 本次调整

- 默认发送块 2048 → 1024，保持原尺寸图片、CRC、保存后 ACK、小段写入和等待时限。整张图片共 151 次发送请求（含头），不是原先 256 字节方案的 601 次。
- 示例波特率设回 115200，与此次实测一致。已有私有配置必须自行确认；不要同时调整块大小和波特率，以便比较结果。
- 板端失败日志增加 frame_elapsed_ms；PC 超时显示接收阶段耗时、最后一批数据之后的无数据时间，并说明将关闭连接。
- CLOSED 仍判定失败，不向已关闭的流重复追加数据，不将半张图片保存为成功。
- LCD 保持回退后的旧版，摄像头方向、内置模型及其余逻辑不变。

这是分块稳定性的对照修订，尚未确认根因或通过实板稳定性测试，不能保证解决所有 CLOSED。没有增加超时时间来掩盖中断。

## 更新

从 `XiUOS-WiFi-Builtin-VM-v8.zip` 取出 detector/photo_wifi.h，覆盖虚拟机原检测目录的同名文件。已安装 v7 且回退 LCD 的环境，不需要覆盖其他板端文件。

在原 photo_wifi_config.h 确认（已有定义则修改，避免重复）：

```c
#define PHOTO_WIFI_BOOT_BAUD 115200
#define PHOTO_WIFI_BAUD 115200
#define PHOTO_WIFI_BLOCK_BYTES 1024
```

保持真实 SSID、密码、PC 地址和 TX=7/RX=6。在 XiZi_IIoT 目录执行：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

烧录新 BIN，期待 `[photo-wifi v8] block=1024`，实际 baud=115200。

本机 pc_bridge/tcp_bridge.py 已更新，Ctrl+C 退出旧接收窗口并重新运行 Start-WiFi-Photo.cmd。另一台 PC 使用包内 Windows/tcp_bridge.py 覆盖原文件，保留 bridge.py。

连续测试并保留两端对应同一 id 的输出。若仍 CLOSED，请提供从 sending 到 failed 的整段板端日志和 PC 超时日志，以对比 frame_elapsed_ms 与 PC 的无数据时间；下一步需要 AT 固件版本或抓包区分 TCP 关闭来源，不再盲目提高速率或超时。

本机验证已通过：真实 C 头文件的完整 1024 字节分块传输、Recv 1024 bytes 后 CLOSED 不误报成功/不重复发送、超时和 ACK 回归、C 数据帧到 Python 保存，以及 16 项 PC 回归测试。未进行实板验证或 RISC-V 交叉编译。
