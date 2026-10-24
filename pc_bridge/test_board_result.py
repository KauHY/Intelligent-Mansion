"""识别结果回传板端（第二阶段 JSON）的专项回归测试。

覆盖四类行为：
1. 旧固件（ACK 后立即关闭连接，不读 JSON）：sendall 触发 OSError/EPIPE，
   回传失败只记日志，不影响识别主流程，也不抛异常。
2. 新固件（按长度前缀读完 JSON 后关闭或回 1 字节）：JSON 完整送达，报文
   结构为 4 字节长度前缀 + JSON 本体，shutdown(SHUT_WR) 后板端仍可读完。
3. boardText 短文案映射与姓名拼接规则（板端 LCD 单行显示用）。
4. 心跳计数：送达计 board_json，板端回执额外计 board_ack。
"""
import json
import socket
import struct
import threading
import unittest

from api_bridge import send_result_to_board, board_text, STATE

RESULT = {
    'status': 1,
    'message': '签到成功',
    'personName': '张三',
    'employeeNo': '1001',
    'personType': 1,
    'participantCategory': 1,
    'meetingReserveId': 'm-1',
    'meetingSubject': '周会',
    'requestId': '1-1',
}


class _Server:
    """socketpair 的服务端一端，收集收到的字节并控制回执行为。"""

    def __init__(self, sock, *, reply=b'', sleep=0):
        self.sock = sock
        self.reply = reply
        self.sleep = sleep
        self.data = b''
        self.peer_closed = False

    def run(self):
        # 完整收完长度前缀 + JSON，然后按场景回执或直接关闭。
        deadline = 5
        self.sock.settimeout(deadline)
        header = self._recv_exact(4)
        if header is None:
            return
        size = struct.unpack('!I', header)[0]
        self.data = self._recv_exact(size)
        if self.sleep:
            self.sock.settimeout(deadline)
            self._recv_exact(1)  # 等 shutdown(SHUT_WR) 到达
            if self.reply:
                self.sock.sendall(self.reply)
        if not self.reply:
            self.sock.close()
            self.peer_closed = True

    def _recv_exact(self, size):
        data = bytearray()
        try:
            while len(data) < size:
                block = self.sock.recv(size - len(data))
                if not block:
                    break
                data.extend(block)
        except OSError:
            pass
        return bytes(data) if len(data) == size else None


def _pair(server_behavior):
    a, b = socket.socketpair()
    server = _Server(a, **server_behavior)
    thread = threading.Thread(target=server.run)
    thread.start()
    return b, server, thread


class BoardResultTests(unittest.TestCase):
    def setUp(self):
        STATE['board_json'] = 0
        STATE['board_ack'] = 0

    def exchange(self, **behavior):
        conn, server, thread = _pair(behavior)
        send_result_to_board(conn, 7, RESULT)
        conn.close()
        thread.join(5)
        self.assertFalse(thread.is_alive())
        return server

    def test_new_firmware_reads_full_json_then_eof(self):
        server = self.exchange()
        payload = server.data
        self.assertTrue(payload)
        record = json.loads(payload.decode('utf-8'))
        for key in ('status', 'message', 'personName', 'employeeNo', 'personType',
                    'participantCategory', 'meetingReserveId', 'meetingSubject',
                    'requestId', 'boardText', 'timestamp'):
            self.assertIn(key, record)
        self.assertEqual(record['status'], 1)
        self.assertEqual(record['personName'], '张三')
        self.assertEqual(STATE['board_json'], 1)
        self.assertEqual(STATE['board_ack'], 0)

    def test_old_firmware_closes_before_reading_json(self):
        """旧固件 ACK 后立即关连接：sendall 失败只记 delivered=no，不抛异常。"""
        a, b = socket.socketpair()
        server_closed = threading.Event()

        def close_immediately():
            b.close()  # 模拟板端 memcmp ACK 后 wiz_sock_close
            server_closed.set()
            a.close()
        thread = threading.Thread(target=close_immediately)
        thread.start()
        server_closed.wait(5)
        # 不抛异常即通过：send_result_to_board 内部吞掉 OSError。
        send_result_to_board(a, 7, RESULT)
        a.close()
        thread.join(5)
        self.assertEqual(STATE['board_json'], 0)

    def test_new_firmware_reply_counts_board_ack(self):
        server = self.exchange(reply=b'\x01', sleep=1)
        self.assertEqual(server.data.decode('utf-8')[:9], '{"status"')
        self.assertEqual(STATE['board_json'], 1)
        self.assertEqual(STATE['board_ack'], 1)

    def test_board_text_mapping_and_name(self):
        cases = {
            1: '张三 签到成功', 2: '张三 已签到', 3: '张三 非与会人员',
            4: '未识别', 5: '未在签到时段', 7: '处理失败', 99: '处理失败',
        }
        for status, expected in cases.items():
            with self.subTest(status=status):
                self.assertEqual(board_text({**RESULT, 'status': status, 'personName': '张三'}),
                                 expected)
        self.assertEqual(board_text({**RESULT, 'status': 1, 'personName': None}), '签到成功')


if __name__ == '__main__':
    unittest.main()
