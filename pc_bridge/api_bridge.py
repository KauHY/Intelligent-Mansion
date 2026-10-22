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
        # Reverse 16-bit pixel order without swapping the two bytes in each RGB565 pixel.
        reversed_pixels = bytearray(raw)
        reversed_pixels[0::2] = raw[-2::-2]
        reversed_pixels[1::2] = raw[-1::-2]
        raw = bytes(reversed_pixels)
    try:
        from PIL import Image
        import io
        pixels = []
        for y in range(height):
            row = []
            for x in range(width):
                offset = 2 * (y * width + x)
                v = struct.unpack_from('<H', raw, offset)[0]
                r = ((v >> 11) & 31) << 3
                g = ((v >> 5) & 63) << 2
                b = (v & 31) << 3
                row.append((r, g, b))
            pixels.append(row)
        img = Image.new('RGB', (width, height))
        img.putdata([p for row in pixels for p in row])
        buf = io.BytesIO()
        img.save(buf, format='JPEG', quality=85)
        return base64.b64encode(buf.getvalue()).decode('ascii')
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
            print(
                '[heartbeat] '
                f'time={timestamp()} uptime_seconds={uptime} '
                f'connections={connections} photos_received={received} '
                f'last_result={last_result} meeting_id={meeting_id or "-"}',
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
        result = call_api(api_base, device_id, meeting_reserve_id, photo['id'], photo_b64, device_token)
        log_identification(result)
        with STATE['lock']:
            STATE['received'] += 1
            STATE['last_result'] = f'{result.get("status")}:{result.get("message")}'
        result_json = json.dumps({
            'status': result.get('status', 7),
            'message': result.get('message', 'unknown'),
            'personName': result.get('personName'),
            'employeeNo': result.get('employeeNo'),
            'personType': result.get('personType'),
            'participantCategory': result.get('participantCategory'),
            'meetingReserveId': result.get('meetingReserveId'),
            'meetingSubject': result.get('meetingSubject'),
            'requestId': result.get('requestId'),
            'timestamp': timestamp(),
        }, ensure_ascii=False).encode('utf-8')
        conn.sendall(result_json)
        conn.shutdown(socket.SHUT_WR)
        conn.settimeout(3)
        try:
            conn.recv(1)
        except (TimeoutError, ConnectionError):
            pass
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

