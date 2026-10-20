"""One image per TCP connection; ACK only after CRC and durable PNG save."""
import argparse
import os
from pathlib import Path
import secrets
import socket
import struct
import time
import zlib

from bridge import ROOT, png565

HEADER = struct.Struct('!4sIHHIII')
ACK = struct.Struct('!4sII')


def receive_exact(conn, size, deadline):
    data = bytearray()
    while len(data) < size:
        remaining = deadline - time.monotonic()
        if remaining <= 0:
            raise TimeoutError(f'图片接收超时：已收到 {len(data)}/{size} bytes')
        conn.settimeout(min(remaining, 30))
        try:
            block = conn.recv(min(4096, size - len(data)))
        except TimeoutError as exc:
            raise TimeoutError(f'接收超时：已收到 {len(data)}/{size} bytes（单次等待最多30秒）') from exc
        if not block:
            raise ConnectionError(f'连接断开：已收到 {len(data)}/{size} bytes')
        before = len(data)
        data.extend(block)
        if size > HEADER.size and (len(data)//8192 != before//8192 or len(data)==size):
            print(f'[接收进度] {len(data)}/{size} bytes', flush=True)
    return bytes(data)


def receive_photo(conn, output, timeout=360, **options):
    deadline = time.monotonic() + timeout
    magic, ident, width, height, size, crc, fmt = HEADER.unpack(receive_exact(conn, HEADER.size, deadline))
    if (magic != b'XPH1' or not ident or fmt != 1 or
            not (0 < width <= 640 and width % 2 == 0 and 0 < height <= 480) or
            size != width * height * 2):
        raise ValueError('不支持的图片头/尺寸/格式')
    print(f'[接收] id={ident} {width}x{height}, {size} bytes', flush=True)
    raw = receive_exact(conn, size, deadline)
    if zlib.crc32(raw) != crc:
        raise ValueError('图片 CRC32 校验失败')
    png = png565(raw, width, height, **options)
    output = Path(output)
    output.mkdir(parents=True, exist_ok=True)
    path = output / f'face_{time.strftime("%Y%m%d_%H%M%S")}_{ident}_{secrets.token_hex(4)}.png'
    partial = path.with_suffix('.part')
    try:
        with partial.open('xb') as f:
            f.write(png)
            f.flush()
            os.fsync(f.fileno())
        os.replace(partial, path)
    finally:
        partial.unlink(missing_ok=True)
    print(f'[保存成功] {path}', flush=True)
    conn.settimeout(5)
    conn.sendall(ACK.pack(b'XPA1', ident, crc))
    # Keep connection alive until board consumes ACK and closes; bounded wait.
    try:
        conn.recv(1)
    except (TimeoutError, ConnectionError):
        pass
    return path


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--host', default='0.0.0.0')
    parser.add_argument('--port', type=int, default=9000)
    parser.add_argument('--output', type=Path, default=ROOT / 'tmp_photos')
    parser.add_argument('--timeout', type=float, default=360,
                        help='整张图片接收时限（秒，默认360）；单次等待数据最多30秒')
    parser.add_argument('--no-flip-y', action='store_true')
    parser.add_argument('--swap-pairs', action='store_true')
    parser.add_argument('--bgr', action='store_true')
    args = parser.parse_args()
    if args.timeout <= 0 or not 1 <= args.port <= 65535:
        parser.error('timeout 必须大于 0；port 必须为 1..65535')
    args.output.mkdir(parents=True, exist_ok=True)
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as listener:
        listener.bind((args.host, args.port))
        listener.listen(4)
        print(f'[监听 TCP] {args.host}:{args.port}\n[图片目录] {args.output.resolve()}\nCtrl+C 退出', flush=True)
        print(f'[接收时限] 整张图片 {args.timeout:g} 秒；单次等待数据最多30秒', flush=True)
        while True:
            conn, peer = listener.accept()
            with conn:
                print(f'[连接] {peer}', flush=True)
                try:
                    receive_photo(conn, args.output, args.timeout, flip_y=not args.no_flip_y,
                                  swap_pairs=args.swap_pairs, bgr=args.bgr)
                except (OSError, ValueError) as exc:
                    print(f'[采集失败/确认失败] {exc}；未完成成功确认。若已打印保存成功，文件仍保留。', flush=True)


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('\n接收程序已停止')
