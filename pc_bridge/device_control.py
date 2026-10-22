"""Local HTTP control service for the K210 face check-in bridge."""
import argparse
import json
import subprocess
import sys
import threading
import time
from collections import deque
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path

BRIDGE_DIR = Path(__file__).resolve().parent
LOG_PATH = BRIDGE_DIR / 'device_control.log'
TOKEN = 'local-device-control-token'


def timestamp():
    return time.strftime('%Y-%m-%d %H:%M:%S')


def append_log(line):
    try:
        with LOG_PATH.open('ab') as handle:
            handle.write(f'{line}\n'.encode('utf-8'))
            handle.flush()
    except OSError:
        pass


class BridgeManager:
    def __init__(self):
        self.process = None
        self.meeting_id = None
        self.lock = threading.Lock()

    def status(self):
        with self.lock:
            running = self.process is not None and self.process.poll() is None
            return {
                'running': running,
                'pid': self.process.pid if running else None,
                'meetingReserveId': self.meeting_id if running else None,
                'exitCode': None if running else (self.process.poll() if self.process else None),
            }

    def start(self, options):
        meeting_id = str(options.get('meetingReserveId', '') or '')
        with self.lock:
            if self.process is not None and self.process.poll() is None:
                if self.meeting_id == meeting_id:
                    return {'running': True, 'pid': self.process.pid, 'meetingReserveId': meeting_id, 'message': '识别服务已在当前会议运行'}
                self._stop_locked()

            args = [
                sys.executable, '-X', 'utf8', '-u', str(BRIDGE_DIR / 'api_bridge.py'),
                '--listen-port', str(options.get('listenPort', 9000)),
                '--api-base', options.get('apiBase', 'http://localhost:30011'),
                '--device-id', options.get('deviceId', 'k210-001'),
                '--device-token', options.get('deviceToken', 'board-token'),
                '--meeting-reserve-id', meeting_id,
            ]
            append_log(f'[{timestamp()}] start meeting={meeting_id}')
            self.process = subprocess.Popen(args, cwd=BRIDGE_DIR, stdout=open(LOG_PATH, 'ab'), stderr=subprocess.STDOUT)
            self.meeting_id = meeting_id
            return {'running': True, 'pid': self.process.pid, 'meetingReserveId': meeting_id, 'message': '识别服务已启动'}

    def stop(self):
        with self.lock:
            return self._stop_locked()

    def _stop_locked(self):
        if self.process is None or self.process.poll() is not None:
            self.process = None
            self.meeting_id = None
            return {'running': False, 'message': '识别服务未在运行'}
        process, self.process = self.process, None
        self.meeting_id = None
        process.terminate()
        try:
            process.wait(timeout=3)
        except subprocess.TimeoutExpired:
            process.kill()
            process.wait(timeout=3)
        return {'running': False, 'message': '识别服务已停止'}

    def logs(self, limit=16000):
        if not LOG_PATH.exists():
            return []
        try:
            with LOG_PATH.open('rb') as handle:
                handle.seek(0, 2)
                size = handle.tell()
                handle.seek(max(0, size - limit))
                text = handle.read().decode('utf-8', errors='replace')
            return [line for line in text.splitlines() if line.strip()][-160:]
        except OSError:
            return []


class BoardLogCollector:
    def __init__(self, port='COM3', baud=115200):
        self.port = port
        self.baud = baud
        self.status = 'starting'
        self.detail = ''
        self.stop_event = threading.Event()
        self.status_lock = threading.Lock()

    def set_status(self, status, detail=''):
        with self.status_lock:
            self.status = status
            self.detail = detail
        append_log(f'[{timestamp()}] [board-log] status={status} port={self.port} baud={self.baud} detail={detail}')

    def serial_status(self):
        with self.status_lock:
            return {
                'status': self.status,
                'detail': self.detail,
                'port': self.port,
                'baud': self.baud,
            }

    def run(self):
        serial = None
        import_warned = False
        while not self.stop_event.is_set():
            if serial is None:
                try:
                    import serial as serial_module
                    serial = serial_module
                except ImportError:
                    self.set_status('unavailable', '缺少 pyserial，本机 Python 需要安装 pyserial')
                    if not import_warned:
                        import_warned = True
                    self.stop_event.wait(30)
                    continue

            try:
                connection = serial.Serial(port=self.port, baudrate=self.baud, timeout=0.2, write_timeout=3)
                connection.dtr = False
                connection.rts = False
                self.set_status('connected', '串口已打开')
                buffer = ''
                while not self.stop_event.is_set():
                    chunk = connection.read(512)
                    if not chunk:
                        continue
                    buffer += chunk.decode('utf-8', errors='replace').replace('\r', '\n')
                    while '\n' in buffer:
                        line, buffer = buffer.split('\n', 1)
                        if line.strip():
                            append_log(f'[{timestamp()}] [board] {line.strip()}')
                connection.close()
            except Exception as exc:
                self.set_status('failed', str(exc))
                self.stop_event.wait(5)


manager = BridgeManager()
board_log_collector = BoardLogCollector()


class ControlHandler(BaseHTTPRequestHandler):
    def _send(self, status, payload):
        body = json.dumps(payload, ensure_ascii=False).encode('utf-8')
        self.send_response(status)
        self.send_header('Content-Type', 'application/json; charset=utf-8')
        self.send_header('Content-Length', str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def _authorized(self):
        return self.headers.get('X-Device-Control-Token') == TOKEN

    def do_GET(self):
        if not self._authorized():
            self._send(401, {'success': False, 'message': '未授权'})
            return
        if self.path == '/status':
            status = manager.status()
            status['boardSerial'] = board_log_collector.serial_status()
            self._send(200, {'success': True, 'data': status})
            return
        if self.path == '/logs':
            self._send(200, {'success': True, 'data': manager.logs()})
            return
        self._send(404, {'success': False, 'message': '接口不存在'})

    def do_POST(self):
        if not self._authorized():
            self._send(401, {'success': False, 'message': '未授权'})
            return
        try:
            length = int(self.headers.get('Content-Length', 0))
            payload = json.loads(self.rfile.read(length) or b'{}')
        except (ValueError, json.JSONDecodeError):
            self._send(400, {'success': False, 'message': '请求参数错误'})
            return
        if self.path == '/start':
            if not payload.get('meetingReserveId'):
                self._send(400, {'success': False, 'message': '请选择会议'})
                return
            self._send(200, {'success': True, 'data': manager.start(payload)})
            return
        if self.path == '/stop':
            self._send(200, {'success': True, 'data': manager.stop()})
            return
        self._send(404, {'success': False, 'message': '接口不存在'})

    def log_message(self, format, *args):
        return


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--port', type=int, default=9010)
    parser.add_argument('--board-port', default='COM3')
    parser.add_argument('--board-baud', type=int, default=115200)
    args = parser.parse_args()

    board_log_collector.port = args.board_port
    board_log_collector.baud = args.board_baud
    thread = threading.Thread(target=board_log_collector.run, name='board-log-collector', daemon=True)
    thread.start()

    server = ThreadingHTTPServer(('0.0.0.0', args.port), ControlHandler)
    print(f'[device control] listening on 0.0.0.0:{args.port}, board logs on {args.board_port}:{args.board_baud}', flush=True)
    server.serve_forever()


if __name__ == '__main__':
    main()