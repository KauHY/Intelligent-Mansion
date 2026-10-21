# W5500 连接诊断 v2 增量补丁

此包仅更新 photo_ethernet.h，不覆盖你已填写的 photo_ethernet_config.h，不修改 .config。

先备份，再将包内 APP_Framework/ 合并到 /home/xiuos/test/xiuos/ 下（按你日志中的大写目录名）。最终文件：
/home/xiuos/test/xiuos/APP_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_ethernet.h

在 Ubiquitous/XiZi_IIoT 中执行 make BOARD=edu-riscv64，烧录新生成固件。保持原有有线、内置模型和连续 5 帧配置。

新日志包括 chip、PHY、link、board、mask、gateway、target、socket、连接时限，以及失败时关闭 socket 之前的 SR/IR。chip 正常应为 0x04。link=DOWN 表示没有物理链路；link=UP 不代表 TCP 可达。

电脑上先启动 Start-Ethernet-Photo.cmd，必须看到 [监听 TCP] 0.0.0.0:9000。
执行 ipconfig，确认有线适配器 IPv4 与板端 target 一致，板端 board 与电脑在相同网段且不重复。
执行 netstat -ano | findstr :9000，应有 LISTENING。若程序启动就无输出，直接运行 python -u pc_bridge/tcp_bridge.py 查看错误。
确认 Windows 防火墙允许该有线网络上的 TCP 9000 入站。不要将目标填成虚拟机 NAT 或电脑 Wi-Fi 地址。

默认独立直连示例：电脑 192.168.50.1/24，板端 192.168.50.2/24，目标端口 9000。同网段直连不需要网关。若使用其他网段，应以实际配置为准。

每次失败后需要连续 10 个检测帧无脸才会重新允许触发，或复位开发板。随后连续 5 帧有脸触发新尝试。连接等待在摄像头线程中，原有 10 秒等待会暂停检测；本补丁在 link=DOWN 时提前失败，但不把传输改成后台任务。

这是诊断增强，并不能单凭旧日志确定故障原因；不会自动修改电脑网络或修复错误 IP/防火墙配置。请提供新版从 [photo-ethernet v2] 到失败行的完整日志，以及电脑 ipconfig 的有线适配器和端口监听结果。

已通过实际生产头文件配合 W5500 API 头文件的主机测试，覆盖连接超时、链路断开、芯片版本异常、原有短写/ACK/CRC 和 C 数据包到 Python PNG/ACK 联动。尚未完成本次实板验证。
