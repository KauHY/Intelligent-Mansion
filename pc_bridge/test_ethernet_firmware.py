"""Compile real firmware headers on host; no RISC-V toolchain/hardware simulation."""
import os
from pathlib import Path
import shutil
import socket
import subprocess
import sys
import threading
import time
import zlib
from tcp_bridge import receive_photo, receive_exact, ACK

def main():
    root = Path(__file__).resolve().parents[1]
    out = root / 'tmp/ethernet_firmware_test'
    out.mkdir(parents=True, exist_ok=True)
    firmware = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure'
    for name in ('photo_bridge.h', 'photo_ethernet.h', 'photo_ethernet_config.h', 'photo_toast_font.h', 'photo_status_font.h', 'photo_orientation.h'):
        shutil.copyfile(firmware / name, out / name)
    driver = root / 'XiUOS/Ubiquitous/XiZi_IIoT/board/edu-riscv64/third_party_driver/include'
    (out / 'shell.h').write_text('#define SHELL_EXPORT_CMD(...)\n')
    detector = (firmware / 'k210_yolov2_detect.c').read_text(encoding='utf-8')
    (out / 'actual_crc.h').write_text(detector[detector.index('static uint32_t model_crc32'):detector.index('#include "photo_bridge.h"')])
    compiler = sys.argv[1] if len(sys.argv)>1 else 'gcc'
    env = dict(os.environ)
    env['PATH'] = str(Path(compiler).parent) + os.pathsep + env.get('PATH', '')
    exe = out / 'ethernet_test.exe'
    subprocess.run([compiler, '-std=c99', '-Wall', '-Wextra', '-Werror', '-Wno-unused-function', '-I'+str(out), '-I'+str(driver),
                    str(root / 'pc_bridge/ethernet_firmware_test.c'), '-o', str(exe)], check=True, env=env)
    subprocess.run([str(exe), str(out / 'frame.bin')], check=True, env=env)
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
        packet = (out/'frame.bin').read_bytes()
        b.sendall(packet)
        reply=receive_exact(b, ACK.size, time.monotonic()+3)
        assert ACK.unpack(reply)==(b'XPA1',1,zlib.crc32(packet[24:])),reply
    thread.join(3)
    assert not thread.is_alive() and not errors,errors
    print('PASS actual C binary frame -> TCP PC receiver -> PNG -> ACK')


if __name__ == "__main__":
    main()
