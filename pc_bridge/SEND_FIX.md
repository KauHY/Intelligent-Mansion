# v5：发送稳定性调整与双端进度诊断

PC 已收到图片头但未收完整个153600字节负载，板端等待 SEND OK 超时。TCP连接和文件目录并非当前已知阻塞点；现有日志不能确认是UART丢字节、回复丢失还是网络发送慢。不能直接把半张图片当作成功保存。

本次采用保守发送：每次 CIPSEND 最多256字节，UART每次写最多64字节、间隔2ms；等待SEND OK从5秒改为15秒。可用PHOTO_WIFI_BLOCK_BYTES覆盖块大小（64..1024），现阶段保留默认256。块确认仍不等于PC保存，只有最终CRC/落盘确认才显示LCD成功。

## 更新

1. 虚拟机只替换 detector/photo_wifi.h 到原检测目录。保持TX=7、RX=6，模型内置和本地网络参数不变，暂时保持目标UART速率115200。
2. 在XiZi_IIoT目录执行：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

烧录本次新BIN。应能看到 `[photo-wifi v5] block=256 UART burst=64 gap=2ms SEND_OK timeout=15s`。
3. Windows项目中的pc_bridge/tcp_bridge.py已同步更新。**在旧接收窗口按Ctrl+C退出，然后重新运行Start-WiFi-Photo.cmd**。不要同时运行两个接收程序。如果在另一台Windows上测试，将包内Windows/tcp_bridge.py复制到项目的pc_bridge/，保留原bridge.py供其导入。
4. 先启动PC接收器，再复位板子并让人脸进入画面。无需重新设置Wi-Fi或模型。

## 判断进度

板端 `confirmed wire=...` 是得到SEND OK确认的累计字节，包含24字节头；完整值为153624。PC `[接收进度] .../153600 bytes` 只统计像素负载。

失败时板端记录：
- block offset：当前块在整个传输流中的偏移，包含头；
- length：当前请求的块长度；
- module_received：模块报告的 `Recv N bytes`，-1表示未解析到该报告，不能直接等同于模块没有收到数据；
- rx_delta：本块期间读到的回复字节数；
- elapsed_ms：本块耗时。

PC超时会显示实际收到的字节数。单次无新数据等待最多30秒，总接收上限仍为120秒。板端发送阶段仍以90秒为限，当前块等待可能额外延长最多15秒。失败不会盲目重发同一块，避免重复数据造成CRC错误。

如果失败，请保留板端v5标识之后的完整日志，以及PC的进度/超时行。两端字节数可以区分最后一块已到PC但回复丢失、数据停在更早位置等情况。PC收到全部数据但确认丢失时，已经保存的文件会保留。

主机测试通过：256字节分块、短写、6秒迟到SEND OK、丢失SEND OK不重发、完整C二进制数据到Python保存/确认；16项接收端回归通过。未实板验证，因此这些是稳定性调整和诊断增强，不能宣称已确定或根治具体丢包原因。
