"""XiZi/K210 serial photo receiver. Only ACK after an atomic, durable PNG save."""
import argparse
import binascii
import os
from pathlib import Path
import re
import secrets
import struct
import time
import zlib

ROOT = Path(__file__).resolve().parents[1]
DETECT = re.compile(rb"detect_info\.obj_number:\s*(\d+)")
RECORD = re.compile(rb"@PHOTO (BEGIN|DATA|END) ([^\r\n]+)")


def png565(raw, width, height, *, flip_y=True, swap_pairs=False, bgr=False):
    """Decode little endian display buffer; row order follows XiZi LCD driver."""
    rows = []
    for y in (range(height - 1, -1, -1) if flip_y else range(height)):
        row = bytearray(b"\0")
        for x in range(width):
            xx = x ^ 1 if swap_pairs else x
            v = struct.unpack_from('<H', raw, 2 * (y * width + xx))[0]
            r, g, b = (v >> 11) & 31, (v >> 5) & 63, v & 31
            r, g, b = (r << 3) | (r >> 2), (g << 2) | (g >> 4), (b << 3) | (b >> 2)
            row.extend((b, g, r) if bgr else (r, g, b))
        rows.append(row)
    def chunk(kind, data):
        return struct.pack('>I', len(data)) + kind + data + struct.pack('>I', zlib.crc32(kind + data))
    return (b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 2, 0, 0, 0))
            + chunk(b'IDAT', zlib.compress(b''.join(rows))) + chunk(b'IEND', b''))


class Bridge:
    def __init__(self, send, output=ROOT / 'tmp_photos', timeout=90, absent_frames=10,
                 flip_y=True, swap_pairs=False, bgr=False, clock=time.monotonic):
        self.send, self.output, self.timeout, self.clock = send, Path(output), timeout, clock
        self.absent_frames = absent_frames
        self.options = dict(flip_y=flip_y, swap_pairs=swap_pairs, bgr=bgr)
        self.buffer = bytearray()
        self.pending = None
        self.frame = None
        self.armed = True
        self.absent = 0
        self.deadline = 0

    def request(self):
        self.pending = secrets.randbelow(0x7ffffffe) + 1
        self.frame = None
        self.armed = False
        self.absent = 0
        self.deadline = self.clock() + self.timeout
        self.send(f'photo_capture {self.pending}\r'.encode('ascii'))
        print(f'[采集请求] {self.pending}')

    def tick(self):
        if self.pending is not None and self.clock() > self.deadline:
            print('[采集失败] 接收超时；检查固件、串口和日志干扰。未发送保存成功确认。')
            self.pending = self.frame = None
            # Require absence/reappearance after failure too; no endless retry storm.

    def feed(self, data):
        self.buffer.extend(data)
        while b'\n' in self.buffer:
            line, _, tail = self.buffer.partition(b'\n')
            self.buffer = bytearray(tail)
            self.line(bytes(line).rstrip(b'\r'))
        if len(self.buffer) > 4096:
            self.buffer.clear()
        self.tick()

    def line(self, line):
        record = RECORD.search(line)
        if record:
            try:
                self.record(record[1].decode(), record[2].decode('ascii').split())
            except (ValueError, IndexError, OSError, struct.error) as exc:
                print(f'[采集失败] {exc}；未确认保存')
                self.pending = self.frame = None
            return
        if line:
            print(line.decode('utf-8', errors='replace'))
        for match in DETECT.finditer(line):
            if self.pending is not None:
                continue
            if int(match[1]) == 0:
                self.absent += 1
                if self.absent >= self.absent_frames:
                    self.armed = True
            else:
                self.absent = 0
                if self.armed:
                    self.request()

    def record(self, kind, fields):
        ident = int(fields[0])
        if ident != self.pending:
            return
        if kind == 'BEGIN':
            if len(fields) != 6 or self.frame is not None:
                raise ValueError('重复或错误的帧头')
            _, w, h, size, fmt, crc = fields
            w, h, size = int(w), int(h), int(size)
            if not (0 < w <= 640 and w % 2 == 0 and 0 < h <= 480 and size == w * h * 2 and fmt == 'RGB565LE'):
                raise ValueError('不支持的图像尺寸/格式')
            self.frame = (w, h, size, int(crc, 16), bytearray())
        elif kind == 'DATA':
            if len(fields) != 3 or self.frame is None:
                raise ValueError('数据块缺少帧头')
            raw = self.frame[4]
            block = bytes.fromhex(fields[2])
            if int(fields[1]) != len(raw) or not 0 < len(block) <= 64 or len(raw) + len(block) > self.frame[2]:
                raise ValueError('数据块丢失、重复或越界')
            raw.extend(block)
        elif kind == 'END':
            if len(fields) != 1 or self.frame is None:
                raise ValueError('结束标记缺少帧头')
            w, h, size, crc, raw = self.frame
            if len(raw) != size or zlib.crc32(raw) != crc:
                raise ValueError('图像长度或 CRC32 校验失败')
            self.output.mkdir(parents=True, exist_ok=True)
            name = f'portrait_{time.strftime("%Y%m%d_%H%M%S")}_{ident}_{secrets.token_hex(3)}.png'
            path = self.output / name
            temp = path.with_suffix('.part')
            try:
                with temp.open('xb') as f:
                    f.write(png565(raw, w, h, **self.options))
                    f.flush()
                    os.fsync(f.fileno())
                os.replace(temp, path)
            finally:
                temp.unlink(missing_ok=True)
            print(f'[照片已保存] {path}')
            self.send(f'photo_saved {ident}\r'.encode('ascii'))
            self.pending = self.frame = None


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--port', default='COM3')
    parser.add_argument('--baud', type=int, default=115200)
    parser.add_argument('--output', type=Path, default=ROOT / 'tmp_photos')
    parser.add_argument('--timeout', type=float, default=90)
    parser.add_argument('--absent-frames', type=int, default=10)
    parser.add_argument('--no-flip-y', action='store_true')
    parser.add_argument('--swap-pairs', action='store_true')
    parser.add_argument('--bgr', action='store_true')
    args = parser.parse_args()
    if args.timeout <= 0 or args.absent_frames < 1:
        parser.error('timeout 和 absent-frames 必须大于 0')
    try:
        import serial
    except ImportError:
        parser.exit(1, '请先运行 python -m pip install -r pc_bridge/requirements.txt\n')
    try:
        # Set control lines before opening; do not deliberately reset the board.
        with serial.Serial(port=None, baudrate=args.baud, timeout=0.2, write_timeout=3,
                           xonxoff=False, rtscts=False, dsrdtr=False) as port:
            port.dtr = port.rts = False
            port.port = args.port
            port.open()
            def send(data):
                if port.write(data) != len(data):
                    raise OSError('串口命令未完整发送')
                port.flush()
            bridge = Bridge(send, args.output, args.timeout, args.absent_frames,
                            not args.no_flip_y, args.swap_pairs, args.bgr)
            print(f'监听 {args.port} / {args.baud} / 8N1，照片目录：{args.output.resolve()}；Ctrl+C 退出')
            while True:
                bridge.feed(port.read(port.in_waiting or 1))
    except KeyboardInterrupt:
        print('\n接收已停止')
    except (serial.SerialException, OSError) as exc:
        parser.exit(1, f'串口连接失败：{exc}。请关闭占用 COM3 的串口终端，检查连接后重新运行。\n')


if __name__ == '__main__':
    main()
