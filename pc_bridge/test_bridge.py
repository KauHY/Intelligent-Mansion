import contextlib
import io
from pathlib import Path
import struct
import tempfile
import unittest
from unittest.mock import patch
import zlib

from bridge import Bridge, png565


def wire(ident, raw=b'\x00\xf8\xe0\x07\x1f\x00\xff\xff', crc=None):
    crc = zlib.crc32(raw) if crc is None else crc
    return (f'\n@PHOTO BEGIN {ident} 2 2 8 RGB565LE {crc:08x}\n'
            f'@PHOTO DATA {ident} 0 {raw.hex()}\n@PHOTO END {ident}\n').encode()


class BridgeTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.sent = []
        self.now = 0
        self.bridge = Bridge(self.sent.append, self.temp.name, absent_frames=3, clock=lambda: self.now)
        self.quiet = contextlib.redirect_stdout(io.StringIO())
        self.quiet.__enter__()
        self.addCleanup(self.quiet.__exit__, None, None, None)

    def trigger(self):
        self.bridge.feed(b'0: (1,2,3,4) cls: face conf: 0.9 detect_info.obj_number:1\r\n')
        return self.bridge.pending

    def test_one_byte_fragments_and_durable_save_before_ack(self):
        ident = self.trigger()
        def send(data):
            self.assertEqual(len(list(Path(self.temp.name).glob('*.png'))), 1)
            self.sent.append(data)
        self.bridge.send = send
        for byte in wire(ident):
            self.bridge.feed(bytes([byte]))
        self.assertEqual(self.sent[-1], f'photo_saved {ident}\r'.encode())
        self.assertFalse(list(Path(self.temp.name).glob('*.part')))

    def test_presence_rearms_only_after_absence(self):
        ident = self.trigger()
        self.bridge.feed(wire(ident))
        self.bridge.feed(b'detect_info.obj_number:1\n' * 30)
        self.assertEqual(len(self.sent), 2)
        self.bridge.feed(b'detect_info.obj_number:0\n' * 3 + b'detect_info.obj_number:2\n')
        self.assertEqual(len(self.sent), 3)

    def test_bad_crc_no_ack(self):
        self.bridge.feed(wire(self.trigger(), crc=0))
        self.assertEqual(len(self.sent), 1)
        self.assertFalse(list(Path(self.temp.name).iterdir()))

    def test_missing_chunk_no_ack(self):
        self.bridge.feed(wire(self.trigger()).replace(b' DATA ', b' LOST '))
        self.assertEqual(len(self.sent), 1)

    def test_duplicate_chunk_no_ack(self):
        data = wire(self.trigger())
        line = data.splitlines()[2] + b'\n'
        self.bridge.feed(data.replace(line, line + line))
        self.assertEqual(len(self.sent), 1)

    def test_wrong_id_ignored(self):
        ident = self.trigger()
        self.bridge.feed(wire(ident + 1))
        self.assertEqual(self.bridge.pending, ident)
        self.assertEqual(len(self.sent), 1)

    def test_save_failure_no_ack(self):
        ident = self.trigger()
        with patch('bridge.os.replace', side_effect=OSError('disk error')):
            self.bridge.feed(wire(ident))
        self.assertEqual(len(self.sent), 1)
        self.assertFalse(list(Path(self.temp.name).iterdir()))

    def test_timeout_and_oversized_line(self):
        self.trigger()
        self.now = 91
        self.bridge.feed(b'x' * 10000)
        self.assertIsNone(self.bridge.pending)
        self.assertEqual(len(self.bridge.buffer), 0)
        self.assertEqual(len(self.sent), 1)

    def test_invalid_dimensions(self):
        ident = self.trigger()
        self.bridge.feed(wire(ident).replace(b'2 2 8 RGB', b'99999 2 8 RGB'))
        self.assertEqual(len(self.sent), 1)

    def test_png_pixel_order(self):
        png = png565(b'\x00\xf8\xe0\x07\x1f\x00\xff\xff', 2, 2)
        offset = 8
        data = b''
        while offset < len(png):
            size = struct.unpack_from('>I', png, offset)[0]
            kind = png[offset + 4:offset + 8]
            body = png[offset + 8:offset + 8 + size]
            if kind == b'IDAT': data += body
            offset += size + 12
        self.assertEqual(zlib.decompress(data), b'\0\0\0\xff\xff\xff\xff\0\xff\0\0\0\xff\0')


if __name__ == '__main__':
    unittest.main()
