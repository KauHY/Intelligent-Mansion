# W5500 有线图像接收端

本目录提供 edu-riscv64 / K210 开发板的 TCP 图像接收程序。当前项目采用 **网线传输、内置 FACE 模型、连续 5 帧人脸确认后上传一张照片**。

完整的板端配置、编译烧录及网络设置见 [项目 README](../README.md)。电脑接收程序应运行在实际与开发板联网的 Windows 电脑上；编译虚拟机可以位于另一台电脑。

## 启动

需要 Python 3.10+，TCP 接收端只使用标准库，无需安装额外依赖。在项目根目录双击 [Start-Ethernet-Photo.cmd](../Start-Ethernet-Photo.cmd)，或执行：

```powershell
python -u pc_bridge/tcp_bridge.py --port 9000
```

如果当前位于 `pc_bridge/` 目录：

```powershell
python -u tcp_bridge.py --port 9000
```

看到 `[监听 TCP] 0.0.0.0:9000` 后保持窗口打开，等待开发板连接。按 `Ctrl+C` 退出。照片默认保存到项目 `tmp_photos/`，文件名为 `face_日期_时间_图片ID_随机后缀.png`，默认目录不受启动位置影响。

部署时保留启动脚本与 `pc_bridge/` 的相对位置，同时复制 `tcp_bridge.py` 和 `bridge.py`。后者提供共用的 RGB565 转 PNG 函数和项目路径，仍是 TCP 接收器的必要依赖。当前流程运行 `tcp_bridge.py`，无需启动 `bridge.py` 的历史串口入口，也不需要安装 `requirements.txt` 中用于串口的依赖。

## 地址和参数

默认电脑有线 IPv4 为 `192.168.50.1`，板端为 `192.168.50.2`，两端掩码为 `255.255.255.0`，端口为 `9000`。同网段直连无需网关或 DNS。板端目标地址由 [photo_ethernet_config.h](../XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_ethernet_config.h) 设置，修改后需重新编译烧录。

| 参数 | 默认值及作用 |
| --- | --- |
| `--host` | `0.0.0.0`，监听本机所有 IPv4 接口；也可指定有线网卡 IP |
| `--port` | `9000`，需与板端 `PHOTO_ETH_PORT` 相同 |
| `--output` | 项目 `tmp_photos/`；指定相对路径时按当前工作目录解析 |
| `--timeout` | 整张图接收时限 360 秒；单次等待新数据最多 30 秒 |
| `--no-flip-y` | 关闭默认的上下翻转 |
| `--swap-pairs` | 交换相邻两个像素的位置 |
| `--bgr` | 交换红蓝通道 |

```powershell
python -u pc_bridge/tcp_bridge.py --host 192.168.50.1 --port 9000 --output ./tmp_photos
```

颜色和方向参数应根据实拍结果调整。接收端超时与板端超时独立：当前板端连接等待上限 10 秒、发送 30 秒、保存确认 10 秒；增大 PC 超时不会改变板端等待时间。

连续帧数在板端 menuconfig 设置：`PHOTO_FACE_CONFIRM_FRAMES=5`、`PHOTO_FACE_ABSENT_FRAMES=10`。接收端没有 `--absent-frames` 参数。

## 采集与保存确认

1. 板端连续 5 个完成检测的帧都有目标时建立 TCP 连接，发送第 5 帧的完整 RGB565 图像；任何无脸帧都会中断连续计数。
2. 电脑检查头部、尺寸、载荷长度和 CRC32，转换为 PNG。
3. 先写 `.part` 临时文件，再执行 flush、fsync 和原子改名，之后回传图片 ID 和 CRC。
4. 板端只有收到匹配确认才在 LCD 显示成功两秒。

一次 TCP 连接传一张图，接收器依次处理连接。默认持续有人只尝试一次；成功或失败后都要连续 10 帧无脸才能重新允许采集。它不进行人员身份识别。连接、传输及提示期间，板端暂停下一帧检测。

CRC、头部、接收或保存失败时不发送成功确认。若 PNG 已保存但 ACK 发送失败，文件仍保留，板端可能不显示成功；不能把电脑保存成功等同于板端已收到确认。

## 二进制协议

头部为 24 字节，Python 格式 `!4sIHHIII`，整数均为网络字节序：

| 偏移 | 长度 | 含义 |
| --- | --- | --- |
| 0 | 4 | `XPH1` |
| 4 | 4 | 非零图片 ID |
| 8 | 2 | 宽度 |
| 10 | 2 | 高度 |
| 12 | 4 | 载荷字节数，等于宽 × 高 × 2 |
| 16 | 4 | 原始载荷 CRC32 |
| 20 | 4 | 像素格式，当前为 `1`（RGB565LE） |

头部之后紧跟原始像素载荷，像素自身为小端 RGB565。当前 320×240 图像载荷为 153600 字节。接收端允许宽度为 1–640 中的偶数、高度为 1–480。

成功确认是 12 字节，格式为 `!4sII`：`XPA1`、图片 ID、载荷 CRC32。TCP 可能拆分或合并数据，接收器按长度累计读取。

## 连接检查

串口终端仅用于板端日志，可与 TCP 接收端同时打开。启动接收端后，在**实际接收电脑**执行：

```powershell
ipconfig
netstat -ano | findstr :9000
ping -n 4 192.168.50.2
arp -a
```

- 有线网卡 IP 应与板端 `target` 相同，端口应显示 `LISTENING`。
- 板端至少尝试一次传图后再检查 ping/ARP，此时才应用配置中的地址。
- 默认板端 MAC 为 `02-58-49-55-00-01`，在 `192.168.50.1` 接口下检查 ARP。ping 失败不能单独证明防火墙阻止连接。
- 防火墙放行 TCP 9000 的精确规则见 [项目 README](../README.md)。

| 日志或现象 | 含义与处理 |
| --- | --- |
| 没有监听提示 | 命令行启动以查看 Python、地址绑定或端口占用错误 |
| 板端 `chip=0x04`、`link=UP` | 芯片版本及物理链路检查通过，尚不能证明 TCP 可达 |
| `link=DOWN` | 检查网线、RJ45 及电脑网卡状态 |
| `failed stage=connect` | 检查目标地址、ARP、监听和防火墙，保留 `reason/SR/IR` 日志 |
| `rc=0`、`SR=0x13` | 非阻塞接口仍在处理，socket 为 INIT，不能当成连接成功 |
| 有脸后短暂卡顿 | 当前连接等待在检测线程中，默认上限约 10 秒 |
| 失败后不重试 | 离开至少 10 个检测帧后再进入，或复位开发板 |
| 已保存但 LCD 未提示 | 检查保存确认及 LCD 配置 |

## 测试和当前状态

在项目根目录运行：

```powershell
python -m unittest discover -s pc_bridge -p "test_tcp_bridge.py" -v
python pc_bridge/test_ethernet_firmware.py "D:/Software Install/CodeBlocks/MinGW/bin/gcc.exe"
```

GCC 路径按实际安装位置替换。第一条验证 TCP 分片接收、校验、保存失败、短载荷与超时；第二条编译真实固件头文件，模拟 W5500 API 并验证连续帧、短写、ACK、超时、链路检查和 C 数据包到 PNG/ACK 的联动。

上述主机测试已通过。实板已经运行人脸检测和有线诊断，最近反馈为 `chip=0x04`、`link=UP`，但 TCP 连接超时；实板图片上传、LCD 成功提示和实际速率尚未验收。主机模拟吞吐量不能作为开发板速率。

本目录其他历史模式脚本和阶段性记录不作为当前部署入口，当前流程以本说明和项目 README 为准。
