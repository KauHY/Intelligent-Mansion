# v13 稳定性验证与异常诊断修订

## 已确认的事实

- 用户提供的 `bins/XiZi-edu-riscv64.elf` 与 `bins/XiZi-edu-riscv64-v12.bin` 可加载内容逐字节一致（457592 字节）。
- 日志显示模型完整读入 388776 字节，CRC32 为预期的 `40429f51`，region layer 初始化成功。
- 检测持续输出 0 后进入 CPU 异常；该段日志没有拍照传输开始的证据。
- 异常输出中两个 CPU 的信息交错，旧异常路径使用的 `KPrintf` 有共享静态缓冲区，不能可靠恢复原始 PC。

这些事实不支持把问题归因于 U 盘模型读坏或 PC 保存路径。尚未取得清晰故障 PC，不能确定崩溃的具体源码行。

## 修订内容

1. XiZi SMP 下检测线程使用“创建 → 绑定 CPU0 → 启动”。原 `pthread_create` 会立即启动，不能在其后可靠地绑定正在运行的任务。CPU0 与当前板级外设中断配置一致。此项避免检测线程跨核迁移，是待实板验证的缓解措施，不是已证实的根因修复。
2. 保留每帧及分块发送时的短延时，让 shell 有机会处理命令；保持模型、串口速度及拍照协议不变。
3. edu-riscv64 启用 UARTHS 时，致命异常输出改为独立的 `[FATAL v13]` 行。直接轮询 UART、不调用 printf/设备锁/任务列表，并通过独立原子锁串行输出两个核的报告。包含 hart、mcause、mepc、mtval、mstatus 和异常帧地址，不访问可能损坏的异常帧。
4. 其他板卡仍使用原异常处理。新的致命异常输出仍会停机等待复位，不会尝试恢复损坏的任务。

当前没有改动内核通用调度器或浮点上下文切换，避免在未确认故障地址前引入更大范围修改。

## 同步和构建

`output/photo_bridge_v13_sources.zip` 是源码覆盖包，**不是可烧录 BIN**。解压后以项目根目录为基准，将以下文件同步到现有 Linux 构建目录对应位置：

```text
XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/k210_yolov2_detect.c
XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_bridge.h
XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_toast_font.h
XiUOS/Ubiquitous/XiZi_IIoT/arch/risc-v/k210/interrupt.c
XiUOS/Ubiquitous/XiZi_IIoT/arch/risc-v/k210/fatal_report.h
```

备份并保留已调通的构建配置。在 `XiUOS/Ubiquitous/XiZi_IIoT` 下重新构建：

```sh
make BOARD=edu-riscv64 clean
make BOARD=edu-riscv64
```

正常构建后，将本次生成的 BIN 和 ELF 配对保存为 v13，使用 K-Flash 烧录新的 BIN。不要把已有 v12 BIN 改名为 v13。

启动应出现：

```text
[photo v13] detector bound to CPU0 before startup
```

若启用 SMP 却看不到该标记，先确认烧录文件和源码同步正确。

## 实板验证顺序

1. 只打开普通终端，保持 115200，运行至少 5 分钟，期间让人脸多次进入、离开画面。
2. 若稳定，退出普通终端，启动 `pc_bridge/bridge.py`。等待完整照片传输和保存，确认 LCD 中文提示显示两秒。
3. 若仍崩溃，保留完整 `[FATAL v13]` 行及本次对应 ELF。关键字段 `mepc` 可映射源码，`mcause` 确定异常类别，`mtval` 辅助判断非法指令或访存地址。不要拿新地址配旧 v12 ELF。

已有 RISC-V 工具链时，可以查询地址（将路径及地址替换为本次真实值）：

```sh
riscv-none-embed-addr2line -e /path/to/v13.elf -f -C 0x实际mepc地址
```

## 测试范围

本次已通过 10 项 PC 接收测试、实际桥接头文件的主机 C 测试、C 输出至 Python 保存 PNG 的联动测试，以及创建/绑定/启动顺序和失败清理的主机 C 测试。

本机没有完整 RISC-V 构建工具链；CPU 绑定在真实内核上的行为、致命异常 UART 输出和完整新固件尚未完成交叉编译及实板验证。覆盖包中的代码不能视为已验证可用的烧录产物。

## 关于波特率

K-Flash 烧录波特率与 XiZi 运行期控制台波特率独立。K-Flash 支持多种高速设置，可以先尝试 921600，稳定后尝试 1500000；是否可用取决于 USB 转串口芯片、驱动及连接。失败时退回较低速度。

当前固件控制台和 PC 程序仍使用 115200。只提高 K-Flash 设置不会提高照片传输速度；提高运行期速度需要同时修改固件 UART 初始化和 PC 连接参数。本轮排查保留 115200。
