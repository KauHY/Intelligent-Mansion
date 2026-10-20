# v4：入网状态通知处理修复

AT ready 表示模块串口通信成功。失败发生在 join AP，尚未连接 PC。
旧代码将所有 WIFI DISCONNECT 通知直接判为失败。v4 在 CWJAP 执行期间继续等待最终 OK 或 FAIL/ERROR；其他阶段断线仍判为失败。仅凭旧日志不能证明此次失败一定是该通知导致，也可能存在真实入网错误。

从 v3 更新只需将 detector/photo_wifi.h 替换到虚拟机原检测目录。保留自己的 photo_wifi_config.h，保持 TX=7、RX=6、K210_MODEL_BUILTIN=y、PHOTO_WIFI_TCP=y。无需再次改 menuconfig。

在原 XiZi_IIoT 目录执行：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

烧录本次生成的 BIN，启动 PC 接收器后复位开发板。成功时应依次看到 WIFI CONNECTED、WIFI GOT IP、connecting PC、sending、PC saved。

如果入网失败，新版会显示 CWJAP error：1=连接超时，2=密码错误，3=找不到热点，4=连接失败，其他值=未知。若只有 ERROR/FAIL 而没有数字码，请保留全部入网日志，不要直接认定密码错误。数值含义参考 Espressif 文档，不同固件可能存在差异：
https://docs.espressif.com/projects/esp-at/en/release-v2.2.0.0_esp8266/AT_Command_Set/Wi-Fi_AT_Commands.html

不会打印密码或包含密码的命令。若仍失败，请提供从 joining AP 到 failed 的全部新日志。

主机回归已覆盖入网断开通知后成功、密码错误后 FAIL、传输阶段断线及完整图片/ACK。尚未实板验证。
