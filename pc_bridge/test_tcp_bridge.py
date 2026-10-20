import socket
import struct
import tempfile
import threading
import unittest
from pathlib import Path
from unittest.mock import patch
import zlib

from tcp_bridge import ACK, HEADER, receive_photo


class TcpTests(unittest.TestCase):
    def exchange(self, packet, *, timeout=1, save_error=False):
        errors, results = [], []
        with tempfile.TemporaryDirectory() as directory:
            a, b = socket.socketpair()
            def server():
                with a:
                    try:
                        results.append(receive_photo(a, directory, timeout))
                    except Exception as exc:
                        errors.append(exc)
            with patch('tcp_bridge.os.replace', side_effect=OSError('disk error')) if save_error else patch('tcp_bridge.secrets.token_hex', return_value='abcd'):
                thread = threading.Thread(target=server)
                thread.start()
                with b:
                    b.settimeout(2)
                    # Fragment both header and binary image, including NUL/LF.
                    for offset in range(0, len(packet), 3):
                        try:
                            b.sendall(packet[offset:offset+3])
                        except OSError:
                            break
                    try:
                        b.shutdown(socket.SHUT_WR)
                        reply = b.recv(64)
                    except ConnectionError:
                        reply = b''  # invalid header can close with unread data
                thread.join(3)
                self.assertFalse(thread.is_alive())
            files = {p.name: p.read_bytes() for p in Path(directory).iterdir()}
        return reply, errors, files

    @staticmethod
    def packet(crc=None, fmt=1, size=8):
        raw = b'\x00\xf8\xe0\x07\x1f\x00\x0a\xff'
        return HEADER.pack(b'XPH1', 123, 2, 2, size, zlib.crc32(raw) if crc is None else crc, fmt) + raw

    def test_saved_ack(self):
        reply, errors, files = self.exchange(self.packet())
        self.assertFalse(errors)
        self.assertEqual(ACK.unpack(reply), (b'XPA1', 123, zlib.crc32(self.packet()[24:])))
        self.assertEqual(len(files), 1)
        self.assertTrue(next(iter(files.values())).startswith(b'\x89PNG'))

    def test_bad_crc_no_ack(self):
        reply, errors, files = self.exchange(self.packet(crc=0))
        self.assertEqual(reply, b'')
        self.assertIsInstance(errors[0], ValueError)
        self.assertFalse(files)

    def test_short_payload_no_ack(self):
        reply, errors, files = self.exchange(self.packet()[:-2])
        self.assertEqual(reply, b'')
        self.assertIsInstance(errors[0], ConnectionError)
        self.assertFalse(files)

    def test_bad_header(self):
        for packet in (self.packet(fmt=2), self.packet(size=600000), self.packet()[:5]):
            reply, errors, files = self.exchange(packet)
            self.assertEqual(reply, b'')
            self.assertTrue(errors)
            self.assertFalse(files)

    def test_failed_save_cleans_partial_no_ack(self):
        reply, errors, files = self.exchange(self.packet(), save_error=True)
        self.assertEqual(reply, b'')
        self.assertIsInstance(errors[0], OSError)
        self.assertFalse(files)

    def test_idle_timeout(self):
        a, b = socket.socketpair()
        with a, b, tempfile.TemporaryDirectory() as directory:
            with self.assertRaises(TimeoutError):
                receive_photo(a, directory, .02)


if __name__ == '__main__':
    unittest.main()
