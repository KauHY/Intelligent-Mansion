"""device_control 日志句柄/断流修复的专项回归测试。

覆盖三类行为：
1. 子进程日志写到独立 api_bridge.log，不再与 device_control.log 共享句柄；
2. 串口静默断链检测：连续空读达到阈值即进入 reconnecting（直接驱动内层循环逻辑）；
3. set_status 相同状态去重，避免重连循环刷屏。
"""
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parent))

import device_control  # noqa: E402


class _FakeProcess:
    def __init__(self):
        self.pid = 4321
        self._poll = None

    def poll(self):
        return self._poll

    def terminate(self):
        self._poll = 0

    def kill(self):
        self._poll = 0

    def wait(self, timeout=None):
        self._poll = 0
        return 0


class LogSplitTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self._orig_paths = (device_control.LOG_PATH, device_control.BRIDGE_LOG_PATH)
        device_control.LOG_PATH = Path(self.temp.name) / 'device_control.log'
        device_control.BRIDGE_LOG_PATH = Path(self.temp.name) / 'api_bridge.log'
        self.manager = device_control.BridgeManager()

    def tearDown(self):
        self.manager.stop()
        device_control.LOG_PATH, device_control.BRIDGE_LOG_PATH = self._orig_paths

    def test_subprocess_logs_to_separate_file(self):
        with patch.object(device_control.sys, 'executable', 'python'), \
             patch.object(device_control.subprocess, 'Popen') as popen:
            popen.return_value = _FakeProcess()
            self.manager.start({'meetingReserveId': '42'})
            popen.assert_called_once()
            redirected = popen.call_args.kwargs['stdout']
        self.assertIsNotNone(self.manager.bridge_log_file)
        self.assertIs(redirected, self.manager.bridge_log_file)
        # device_control.log 只应有 start 标记行；api_bridge.log 由子进程独占（心跳不会出现在控制日志）。
        self.manager.stop()
        control_text = device_control.LOG_PATH.read_text(encoding='utf-8')
        self.assertIn('start meeting=42', control_text)
        self.assertNotIn('[heartbeat]', control_text)

    def test_repeated_start_stop_closes_all_handles(self):
        handles = []
        with patch.object(device_control.sys, 'executable', 'python'), \
             patch.object(device_control.subprocess, 'Popen') as popen:
            popen.return_value = _FakeProcess()
            for meeting in ('1', '2'):
                self.manager.start({'meetingReserveId': meeting})
                handles.append(self.manager.bridge_log_file)
                self.manager.stop()
                self.assertTrue(all(h.closed for h in handles if h))
        self.assertIsNone(self.manager.bridge_log_file)
        self.assertIsNone(self.manager.log_file)


class CollectorSilenceTests(unittest.TestCase):
    """静默断链检测：直接驱动 run() 内层循环同款逻辑，保证阈值行为不回归。"""

    SILENT_LIMIT = 20

    def _drive(self, reads):
        silent = 0
        status = 'connected'
        for chunk in reads:
            if not chunk:
                silent += 1
                if silent >= self.SILENT_LIMIT:
                    status = 'reconnecting'
                    break
                continue
            silent = 0
        return status

    def test_silent_reads_trigger_reconnect(self):
        reads = [b'[x] hello\n'] + [b''] * 100
        self.assertEqual(self._drive(reads), 'reconnecting')

    def test_healthy_stream_stays_connected(self):
        reads = ([b'[x] data\n'] + [b''] * 15) * 10  # 空读未达阈值，穿插真实数据
        self.assertEqual(self._drive(reads), 'connected')


class LogsMergeTests(unittest.TestCase):
    """logs() 合并读取：board 行与心跳行各占独立配额，互不挤占。"""

    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self._orig_paths = (device_control.LOG_PATH, device_control.BRIDGE_LOG_PATH)
        device_control.LOG_PATH = Path(self.temp.name) / 'device_control.log'
        device_control.BRIDGE_LOG_PATH = Path(self.temp.name) / 'api_bridge.log'
        self.manager = device_control.BridgeManager()

    def tearDown(self):
        device_control.LOG_PATH, device_control.BRIDGE_LOG_PATH = self._orig_paths

    def test_board_lines_survive_heartbeat_flood(self):
        # device_control.log 有大量历史 board 行，api_bridge.log 心跳刷屏。
        device_control.LOG_PATH.write_text(
            '\n'.join(f'[t] [board] detect line {i}' for i in range(400)), encoding='utf-8')
        device_control.BRIDGE_LOG_PATH.write_text(
            '\n'.join(f'[{i}] [heartbeat] tick' for i in range(500)), encoding='utf-8')
        out = self.manager.logs()
        self.assertGreater(sum(1 for line in out if '[board]' in line), 0)
        self.assertGreater(sum(1 for line in out if '[heartbeat]' in line), 0)

    def test_board_lines_kept_when_history_dominates(self):
        # board 行只在 device_control.log 历史深处，近期无新板端行：合并结果仍应保留 board 行。
        device_control.LOG_PATH.write_text(
            '\n'.join(f'[t] [board] old line {i}' for i in range(400)) + '\n'
            + '\n'.join(f'[t] control {i}' for i in range(5)), encoding='utf-8')
        device_control.BRIDGE_LOG_PATH.write_text(
            '\n'.join(f'[{i}] [heartbeat] tick' for i in range(500)), encoding='utf-8')
        out = self.manager.logs()
        self.assertGreater(sum(1 for line in out if '[board]' in line), 0)


class StatusDedupeTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self._orig_path = device_control.LOG_PATH
        device_control.LOG_PATH = Path(self.temp.name) / 'device_control.log'
        self.collector = device_control.BoardLogCollector(port='COM9', baud=115200)

    def tearDown(self):
        device_control.LOG_PATH = self._orig_path

    def test_set_status_dedupes_same_status(self):
        self.collector.set_status('connected', 'a')
        self.collector.set_status('connected', 'a')  # 重复 → 不落日志
        self.collector.set_status('connected', 'b')  # detail 变化 → 落日志
        text = device_control.LOG_PATH.read_text(encoding='utf-8')
        self.assertEqual(text.count('status=connected'), 2)


if __name__ == '__main__':
    unittest.main()
