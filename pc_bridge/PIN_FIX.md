# v3：纠正 ESP8285 UART 引脚方向

之前提供的 TX=6、RX=7 配置有误。06 文档表中的 WIFI_TX/WIFI_RX 是 ESP8285 侧信号，不能直接作为 K210 的 TX/RX。

同一开发板的仓库实现提供了交叉映射：

```text
Ubiquitous/Nuttx_Fusion_XiUOS/aiit_board/edu-riscv64/include/board.h
  GPIO_WIFI_TXD = 6
  GPIO_WIFI_RXD = 7

Ubiquitous/Nuttx_Fusion_XiUOS/aiit_board/edu-riscv64/src/k210_bringup.c
  GPIO_WIFI_TXD -> FPOA_USART1_RX
  GPIO_WIFI_RXD -> FPOA_USART1_TX
```

正确连线是：K210 TX(IO7) → ESP RX；ESP TX → K210 RX(IO6)。这里只需要改 K210 的 FPIOA 配置，不需要改板上连线，也不需要复制 NuttX 驱动。

## 从已经成功内置模型的 v2 升级

1. 将本包 detector/photo_wifi.h 和 detector/Kconfig 替换到虚拟机已有检测目录。其余板端代码与 v2 一致，可保持成功运行的版本。
2. 保留 photo_wifi_config.h 中的 Wi-Fi 和 PC 信息，保留 K210_MODEL_BUILTIN=y。
3. 在原构建目录备份并同步配置：

```sh
cd ~/test/xiuos/Ubiquitous/XiZi_IIoT
cp .config .config.before-wifi-pin-fix
cp board/edu-riscv64/.config board/edu-riscv64/.config.before-wifi-pin-fix
cp .config board/edu-riscv64/.config
make BOARD=edu-riscv64 menuconfig
```

搜索并设置：

```text
CONFIG_BSP_UART1_TXD_PIN=7
CONFIG_BSP_UART1_RXD_PIN=6
CONFIG_PHOTO_WIFI_TCP=y
CONFIG_K210_MODEL_BUILTIN=y
```

保存后确认：

```sh
grep -E 'CONFIG_(BSP_UART1_TXD_PIN|BSP_UART1_RXD_PIN|PHOTO_WIFI_TCP|K210_MODEL_BUILTIN)' .config
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

必须同时更新头文件和配置：旧头文件会错误拒绝正确引脚；新头文件会拒绝旧的反向配置。烧录本次新 BIN。

## PC 与实板验收

Windows 项目根目录的 Start-WiFi-Photo.cmd 已增加启动提示和 UTF-8 输出。关闭旧接收窗口，重新双击它，应看到：

```text
[START] XiUOS Wi-Fi photo receiver
[监听 TCP] 0.0.0.0:9000
[图片目录] ...\tmp_photos
```

如果连监听提示都没有，可在项目根目录 PowerShell 中直接执行：

```powershell
.\.venv\Scripts\python.exe -X utf8 -u pc_bridge\tcp_bridge.py
```

若显示端口占用，检查已运行的接收窗口，不要同时重复启动。该启动脚本留在 Windows，不需要放入虚拟机。

先启动 PC 接收端，再复位开发板。新固件日志应显示 `TX=7 RX=6`，然后 `AT ready`、`joining AP`、`connecting PC`。只有出现 `sending` 才开始传图；如果 AT 阶段失败，PC 没有新连接记录是预期现象。

如纠正后仍然所有候选波特率 rx=0，再检查模块使能、模块 AT 固件和 UART 接收。先核对启动日志确实为 TX=7 RX=6，排除旧 BIN 或配置未生效。

主机测试验证正确配置通过、旧配置编译失败，完整图片与 ACK 协议回归通过。硬件引脚方向依据同板源码核对；实际模块响应仍需烧录后确认。
