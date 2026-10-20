# v6：修复持续传输在 90 秒中止

v5 日志显示约 88 秒确认 106496 字节图像，PC 最终收到 111104/153600 字节。板端代码在下一块开始前检查整图 90000ms 上限，超过便直接返回，错误阶段却仍显示上一块的 SEND OK。PC 随后等待 30 秒无新数据而报错。这与本次日志吻合，不是保存目录错误。

按此次实测速度，完整图片约需 125–140 秒；原 PC 总时限 120 秒也不足。v6 将板端发送阶段改为 300 秒、PC 整图接收改为 360 秒。保持单块 SEND OK 15 秒、PC 无数据等待 30 秒、256 字节分块及 UART 115200。时限增加不会加速发送，但允许仍在前进的图片完成传输。CRC 校验和保存后 ACK 保持不变，不保存不完整图片。

## 更新步骤

1. 解压 `output/XiUOS-WiFi-Builtin-VM-v6.zip`。虚拟机只需将包内 `detector/photo_wifi.h` 覆盖原检测目录的同名文件：
   `App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_wifi.h`。
   若虚拟机实际目录是 `APP_Framework`，使用实际目录名，不要另建目录。保持本地 `photo_wifi_config.h`、内置模型和菜单配置不变。
2. 在虚拟机 `~/test/xiuos/Ubiquitous/XiZi_IIoT` 中重新构建：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

3. 烧录本次构建的 BIN。开始传输应出现 `[photo-wifi v6]` 和 `frame_limit_ms=300000`；若仍是 v5，检查替换路径和烧录文件。
4. 本机 `pc_bridge/tcp_bridge.py` 已更新。在原接收窗口按 Ctrl+C，重新双击 `Start-WiFi-Photo.cmd`。应显示整张图片 360 秒。其他 PC 可复制包内 `Windows/tcp_bridge.py` 覆盖原文件，保留项目现有 `bridge.py`。
5. 先启动 PC 接收器，再让板端检测人脸。PC 应到达 `153600/153600`，打印保存成功到项目 `tmp_photos`；板端收到保存确认后显示 LCD 提示。

板端整图上限触发时，现在会明确打印 `frame deadline elapsed_ms=... limit_ms=... confirmed_wire=...`，不再误报为 SEND OK 阶段。上限在每块开始前检查，已开始的块仍有独立等待时限。默认值兼容已有私有配置，不需要替换 Wi-Fi 密码文件。

验证已通过：C 模拟时钟下耗时 142.84 秒的完整传输及保存确认、300 秒整图上限触发、单块回复丢失不重发、C 数据帧到 Python 接收保存，以及 16 项 PC 回归测试。该包是源码更新，不含新编译 BIN。主机模拟不能替代实板验证；如果仍失败，请保留 v6 标识之后的整段板端日志及 PC 进度/失败行。
