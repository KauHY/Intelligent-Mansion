"""Bridge between K210 board TCP photo transfer and smart-park face check-in API.

The bridge prints heartbeat and structured identification logs. The verify
response is also forwarded to the board as JSON, so K210 firmware can display
the person name, employee number, person type and meeting information.
"""
import argparse
import base64
import json
import socket
import struct
import threading
import time
import zlib
from urllib.request import Request, urlopen

HEADER = struct.Struct('!4sIHHIII')
ACK = struct.Struct('!4sII')

STATE = {
    'started_at': 0.0,
    'connections': 0,
    'received': 0,
    'last_result': 'none',
    'meeting_id': '',
    'board_json': 0,
    'board_ack': 0,
    'lock': threading.Lock(),
}


def timestamp():
    return time.strftime('%Y-%m-%d %H:%M:%S')


def receive_exact(conn, size, timeout=30):
    data = bytearray()
    conn.settimeout(timeout)
    while len(data) < size:
        block = conn.recv(min(4096, size - len(data)))
        if not block:
            raise ConnectionError(f'Connection closed: received {len(data)}/{size}')
        data.extend(block)
    return bytes(data)


def receive_photo(conn, timeout=60):
    deadline = time.monotonic() + timeout
    magic, ident, width, height, size, crc, fmt = HEADER.unpack(receive_exact(conn, HEADER.size, timeout))
    if magic != b'XPH1' or not ident or fmt != 1 or not (0 < width <= 640 and width % 2 == 0 and 0 < height <= 480):
        raise ValueError('Unsupported photo header')
    raw = receive_exact(conn, size, max(10, deadline - time.monotonic()))
    if zlib.crc32(raw) != crc:
        raise ValueError('CRC32 mismatch')
    print(f'[photo] time={timestamp()} id={ident} size={width}x{height} bytes={size}', flush=True)
    conn.sendall(ACK.pack(b'XPA1', ident, crc))
    return {'id': ident, 'width': width, 'height': height, 'raw': raw, 'crc': crc}


def rgb565_to_jpeg_base64(raw, width, height, rotate_180=False):
    """Convert raw RGB565 to JPEG using Pillow; falls back to BMP if Pillow unavailable."""
    if rotate_180:
        import numpy as np
        raw = np.frombuffer(raw, dtype='<u2')[::-1].tobytes()

    try:
        import cv2
        import numpy as np
        values = np.frombuffer(raw, dtype='<u2').reshape(height, width)
        blue = ((values & 31) << 3).astype('uint8')
        green = (((values >> 5) & 63) << 2).astype('uint8')
        red = (((values >> 11) & 31) << 3).astype('uint8')
        image = np.stack((blue, green, red), axis=-1)
        success, encoded = cv2.imencode('.jpg', image, [int(cv2.IMWRITE_JPEG_QUALITY), 85])
        if success:
            return base64.b64encode(encoded.tobytes()).decode('ascii')
    except ImportError:
        pass

    pixel_data = bytearray()
    for y in range(height - 1, -1, -1):
        for x in range(width):
            offset = 2 * (y * width + x)
            v = struct.unpack_from('<H', raw, offset)[0]
            r = ((v >> 11) & 31) << 3
            g = ((v >> 5) & 63) << 2
            b = (v & 31) << 3
            pixel_data.extend([b, g, r])
    row_size = (width * 3 + 3) & ~3
    padding = row_size - width * 3
    data_size = row_size * height
    file_size = 54 + data_size
    header = struct.pack('<2sIHHIIiiHHIIiiII',
        b'BM', file_size, 0, 0, 54, 40, width, height, 1, 24, 0, data_size, 2835, 2835, 0, 0)
    padded_rows = bytearray()
    for y in range(height):
        row = pixel_data[y * width * 3:(y + 1) * width * 3]
        padded_rows.extend(row)
        padded_rows.extend(b'\x00' * padding)
    return base64.b64encode(header + bytes(padded_rows)).decode('ascii')


def call_api(api_base, device_id, meeting_reserve_id, request_id, photo_base64, device_token):
    url = f'{api_base}/smartPark/park-meeting/face-checkin/verify'
    payload = json.dumps({
        'deviceId': device_id,
        'deviceToken': device_token,
        'requestId': str(request_id),
        'meetingReserveId': meeting_reserve_id,
        'photoBase64': f'data:image/jpeg;base64,{photo_base64}',
    }).encode('utf-8')
    req = Request(url, data=payload, headers={'Content-Type': 'application/json'}, method='POST')
    try:
        with urlopen(req, timeout=15) as resp:
            body = json.loads(resp.read())
            data = body.get('data', {}) or {}
            return {
                'status': data.get('status', 7),
                'message': data.get('message', 'unknown'),
                'personName': data.get('personName'),
                'employeeNo': data.get('employeeNo'),
                'personType': data.get('personType'),
                'participantCategory': data.get('participantCategory'),
                'meetingReserveId': data.get('meetingReserveId') or meeting_reserve_id,
                'meetingSubject': data.get('meetingSubject'),
                'requestId': data.get('requestId') or str(request_id),
            }
    except Exception as exc:
        print(f'[error] time={timestamp()} stage=api {exc}', flush=True)
        return {
            'status': 7,
            'message': '处理失败',
            'personName': None,
            'employeeNo': None,
            'personType': None,
            'meetingReserveId': meeting_reserve_id,
            'meetingSubject': None,
            'requestId': str(request_id),
        }


def log_identification(result):
    person_type = result.get('personType')
    type_name = {1: '工作人员', 2: '访客'}.get(person_type, '未知')
    print(
        '[identify] '
        f'time={timestamp()} '
        f'meeting_id={result.get("meetingReserveId") or "-"} '
        f'meeting_subject={result.get("meetingSubject") or "-"} '
        f'person={result.get("personName") or "未识别"} '
        f'person_type={type_name} '
        f'employee_no={result.get("employeeNo") or "-"} '
        f'status={result.get("status")} '
        f'message={result.get("message")} '
        f'request_id={result.get("requestId") or "-"}',
        flush=True,
    )


# 板端 LCD 只有单行点阵的显示余量，PC 预先拼好短文案，板端拿到即可渲染。
BOARD_TEXT = {
    1: '签到成功', 2: '已签到', 3: '非与会人员',
    4: '未识别', 5: '未在签到时段', 7: '处理失败',
}


def board_text(result):
    text = BOARD_TEXT.get(result.get('status'), '处理失败')
    name = result.get('personName')
    if name and result.get('status') in (1, 2, 3):
        return f'{name} {text}'
    return text


def send_result_to_board(conn, photo_id, result):
    """识别 JSON 回传板端；任何失败只记日志，绝不影响已完成的识别结果。

    报文 = 4 字节 JSON 长度（网络字节序）+ JSON 本体，随后 shutdown(SHUT_WR)。
    现有板端固件在 ACK 后立即关闭连接，JSON 写入会以 EPIPE/Reset 告终，
    这是预期行为而不是错误；新固件按长度读完 JSON 后关闭或回 1 字节回执。
    """
    payload = json.dumps({
        'status': result.get('status', 7),
        'message': result.get('message', 'unknown'),
        'personName': result.get('personName'),
        'employeeNo': result.get('employeeNo'),
        'personType': result.get('personType'),
        'participantCategory': result.get('participantCategory'),
        'meetingReserveId': result.get('meetingReserveId'),
        'meetingSubject': result.get('meetingSubject'),
        'requestId': result.get('requestId'),
        'boardText': board_text(result),
        'timestamp': timestamp(),
    }, ensure_ascii=False).encode('utf-8')
    try:
        conn.sendall(struct.pack('!I', len(payload)) + payload)
        conn.shutdown(socket.SHUT_WR)
        # 回执窗口：旧固件直接关闭连接（EOF，立即返回）；新固件读完 JSON 后
        # 可以关闭，也可以回 1 字节确认。两者都算送达。
        conn.settimeout(3)
        try:
            reply = conn.recv(1)
        except (TimeoutError, ConnectionError):
            reply = b''
        with STATE['lock']:
            STATE['board_json'] += 1
            STATE['board_ack'] += bool(reply)
        mode = 'ack' if reply else 'eof'
        print(f'[board-result] time={timestamp()} id={photo_id} bytes={len(payload)} '
              f'delivered=yes mode={mode}', flush=True)
    except OSError as exc:
        # ACK 之后板端主动断开时 JSON 写入失败：旧固件不读 JSON，属预期路径。
        print(f'[board-result] time={timestamp()} id={photo_id} bytes={len(payload)} '
              f'delivered=no reason={exc.__class__.__name__}', flush=True)


def start_heartbeat():
    def run():
        while True:
            time.sleep(15)
            with STATE['lock']:
                uptime = int(time.time() - STATE['started_at'])
                connections = STATE['connections']
                received = STATE['received']
                last_result = STATE['last_result']
                meeting_id = STATE['meeting_id']
                board_json = STATE['board_json']
                board_ack = STATE['board_ack']
            print(
                '[heartbeat] '
                f'time={timestamp()} uptime_seconds={uptime} '
                f'connections={connections} photos_received={received} '
                f'last_result={last_result} meeting_id={meeting_id or "-"} '
                f'board_json_delivered={board_json} board_ack={board_ack}',
                flush=True,
            )
    thread = threading.Thread(target=run, name='bridge-heartbeat', daemon=True)
    thread.start()


def handle_client(conn, peer, api_base, device_id, meeting_reserve_id, device_token, rotate_180=False):
    try:
        with STATE['lock']:
            STATE['connections'] += 1
        print(f'[connection] time={timestamp()} peer={peer[0]}:{peer[1]} meeting_id={meeting_reserve_id or "-"}', flush=True)
        photo = receive_photo(conn)
        photo_b64 = rgb565_to_jpeg_base64(
            photo['raw'], photo['width'], photo['height'], rotate_180=rotate_180)
        request_id = f'{int(STATE["started_at"])}-{photo["id"]}'
        result = call_api(api_base, device_id, meeting_reserve_id, request_id, photo_b64, device_token)
        log_identification(result)
        with STATE['lock']:
            STATE['received'] += 1
            STATE['last_result'] = f'{result.get("status")}:{result.get("message")}'
        send_result_to_board(conn, photo['id'], result)
    except (OSError, ValueError, TimeoutError) as exc:
        print(f'[error] time={timestamp()} peer={peer[0]}:{peer[1]} {exc}', flush=True)
    finally:
        with STATE['lock']:
            STATE['connections'] = max(0, STATE['connections'] - 1)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--listen-port', type=int, default=9000)
    parser.add_argument('--api-base', default='http://localhost:30011')
    parser.add_argument('--device-id', default='k210-001')
    parser.add_argument('--meeting-reserve-id', default='')
    parser.add_argument('--device-token', default='board-token')
    parser.add_argument('--rotate-180', action=argparse.BooleanOptionalAction, default=True,
                        help='rotate board photos 180 degrees before inference; disable with --no-rotate-180')
    args = parser.parse_args()

    STATE['started_at'] = time.time()
    STATE['meeting_id'] = args.meeting_reserve_id
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as listener:
        listener.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        listener.bind(('0.0.0.0', args.listen_port))
        listener.listen(4)
        print(f'[start] time={timestamp()} tcp=0.0.0.0:{args.listen_port} api={args.api_base} device={args.device_id} meeting_id={args.meeting_reserve_id or "-"}', flush=True)
        start_heartbeat()
        while True:
            conn, peer = listener.accept()
            with conn:
                handle_client(conn, peer, args.api_base, args.device_id, args.meeting_reserve_id, args.device_token, args.rotate_180)


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print(f'\n[stop] time={timestamp()} bridge stopped.', flush=True)

