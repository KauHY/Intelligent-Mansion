"""Compile real firmware headers on host; no RISC-V toolchain/hardware simulation."""
import os
from pathlib import Path
import shutil
import socket
import subprocess
import sys
import threading
from tcp_bridge import receive_photo, ACK

root = Path(__file__).resolve().parents[1]
out = root / 'tmp/wifi_firmware_test'
out.mkdir(parents=True, exist_ok=True)
firmware = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure'
for name in ('photo_bridge.h', 'photo_wifi.h', 'photo_toast_font.h', 'photo_orientation.h'):
    shutil.copyfile(firmware / name, out / name)
shutil.copyfile(firmware / 'photo_wifi_config.example.h', out / 'photo_wifi_config.h')
(out / 'shell.h').write_text('#define SHELL_EXPORT_CMD(...)\n')
detector = (firmware / 'k210_yolov2_detect.c').read_text(encoding='utf-8')
(out / 'actual_crc.h').write_text(detector[detector.index('static uint32_t model_crc32'):detector.index('#include "photo_bridge.h"')])
compiler = sys.argv[1] if len(sys.argv)>1 else 'gcc'
env = dict(os.environ)
env['PATH'] = str(Path(compiler).parent) + os.pathsep + env.get('PATH', '')
exe = out / 'wifi_test.exe'
subprocess.run([compiler, '-std=c99', '-Wall', '-Wextra', '-Werror', '-Wno-unused-function', '-I'+str(out),
                str(root / 'pc_bridge/wifi_firmware_test.c'), '-o', str(exe)], check=True, env=env)
subprocess.run([str(exe), str(out / 'frame.bin')], check=True, env=env)
# Wrong MCU direction must fail compilation, not silently yield rx=0 at runtime.
wrong = (root / 'pc_bridge/wifi_firmware_test.c').read_text(encoding='utf-8')
wrong = wrong.replace('#define BSP_UART1_TXD_PIN 7', '#define BSP_UART1_TXD_PIN 6')
wrong = wrong.replace('#define BSP_UART1_RXD_PIN 6', '#define BSP_UART1_RXD_PIN 7')
(out / 'wrong_pins.c').write_text(wrong, encoding='utf-8')
rejected = subprocess.run([compiler, '-std=c99', '-fsyntax-only', '-I'+str(out),
                           str(out / 'wrong_pins.c')], capture_output=True, env=env)
assert rejected.returncode != 0 and b'K210 UART1 TX=7' in rejected.stderr
print('PASS wrong TX=6/RX=7 configuration rejected at compile time')
a,b = socket.socketpair()
errors=[]
def server():
    with a:
        try:
            receive_photo(a,out/'received',2)
        except Exception as exc:
            errors.append(exc)
thread=threading.Thread(target=server)
thread.start()
with b:
    b.settimeout(3)
    b.sendall((out/'frame.bin').read_bytes())
    reply=b.recv(12)
    assert ACK.unpack(reply)[0:2]==(b'XPA1',1),reply
thread.join(3)
assert not thread.is_alive() and not errors,errors
print('PASS actual C binary frame -> TCP PC receiver -> PNG -> ACK')
