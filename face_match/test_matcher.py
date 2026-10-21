"""face_match 单元测试。

大部分用例通过 mock 替代真实模型和人脸，因此无需下载模型即可运行：
    python -m unittest discover -s face_match -p "test_*.py" -v
"""
from __future__ import annotations

import os
import tempfile
import unittest
from pathlib import Path
from unittest import mock

import numpy as np

try:
    from . import matcher
except ImportError:  # unittest discover -s face_match 时按顶层模块导入
    import matcher


class _FakeDetector:
    def __init__(self, face_count: int = 1):
        self.face_count = face_count
        self.input_size = None

    def setInputSize(self, size):
        self.input_size = size

    def detect(self, image):
        if self.face_count == 0:
            return 0, None
        faces = np.zeros((self.face_count, 15), dtype=np.float32)
        for index in range(self.face_count):
            faces[index, 2:4] = (10.0 + index, 10.0 + index)
        return self.face_count, faces


class _FakeRecognizer:
    def __init__(self, similarity: float):
        self.similarity = similarity

    def alignCrop(self, image, face):
        return np.zeros((112, 112, 3), dtype=np.uint8)

    def feature(self, aligned):
        return np.zeros((1, 128), dtype=np.float32)

    def match(self, feature1, feature2, flag):
        return self.similarity


class MatchFacesTests(unittest.TestCase):
    def _run(self, similarity=0.8, face_count=1, threshold=matcher.DEFAULT_THRESHOLD):
        detector = _FakeDetector(face_count)
        recognizer = _FakeRecognizer(similarity)
        image = np.zeros((240, 320, 3), dtype=np.uint8)
        with mock.patch.object(
            matcher, "_load_models", return_value=(detector, recognizer)
        ), mock.patch.object(matcher, "read_image", return_value=image):
            return matcher.match_faces("a.png", "b.png", threshold=threshold)

    def test_similarity_above_threshold_is_success(self):
        result = self._run(similarity=0.8)
        self.assertTrue(result.success)
        self.assertAlmostEqual(result.similarity, 0.8)
        self.assertEqual(result.reason, "")

    def test_similarity_below_threshold_is_failed(self):
        result = self._run(similarity=0.2)
        self.assertFalse(result.success)
        self.assertEqual(result.reason, "相似度低于阈值")

    def test_similarity_equal_to_threshold_is_success(self):
        result = self._run(similarity=matcher.DEFAULT_THRESHOLD)
        self.assertTrue(result.success)

    def test_no_face_is_failed_without_exception(self):
        result = self._run(face_count=0)
        self.assertFalse(result.success)
        self.assertIsNone(result.similarity)
        self.assertIn("未检测到人脸", result.reason)

    def test_multiple_faces_note(self):
        result = self._run(face_count=2)
        self.assertTrue(result.success)
        self.assertEqual(result.face_count1, 2)
        self.assertIn("面积最大", result.note)

    def test_invalid_threshold(self):
        with self.assertRaises(ValueError):
            matcher.match_faces("a.png", "b.png", threshold=1.5)
        with self.assertRaises(ValueError):
            matcher.match_faces("a.png", "b.png", det_threshold=0)

    def test_missing_image_raises(self):
        with self.assertRaises(matcher.FaceMatchError):
            matcher.read_image(Path("definitely-not-here.png"))


class ModelTests(unittest.TestCase):
    def test_no_download_reports_manual_url(self):
        with tempfile.TemporaryDirectory() as tmp:
            with self.assertRaises(matcher.FaceMatchError) as ctx:
                matcher.ensure_models(tmp, allow_download=False)
            message = str(ctx.exception)
            self.assertIn(matcher.YUNET_NAME, message)
            self.assertIn("opencv_zoo", message)

    def test_existing_files_are_accepted(self):
        with tempfile.TemporaryDirectory() as tmp:
            directory = Path(tmp)
            with (directory / matcher.YUNET_NAME).open("wb") as handle:
                handle.truncate(200 * 1024)
            with (directory / matcher.SFACE_NAME).open("wb") as handle:
                handle.truncate(30 * 1024 * 1024)
            yunet, sface = matcher.ensure_models(directory, allow_download=False)
            self.assertEqual(yunet.name, matcher.YUNET_NAME)
            self.assertEqual(sface.name, matcher.SFACE_NAME)

    def test_too_small_file_is_rejected(self):
        with tempfile.TemporaryDirectory() as tmp:
            directory = Path(tmp)
            (directory / matcher.YUNET_NAME).write_bytes(b"<html>error</html>")
            with self.assertRaises(matcher.FaceMatchError):
                matcher.ensure_models(directory, allow_download=False)


@unittest.skipUnless(matcher.cv2 is not None, "需要安装 opencv-python")
class ImageReadTests(unittest.TestCase):
    def test_invalid_image_bytes_raise(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "bad.png"
            path.write_bytes(b"not an image")
            with self.assertRaises(matcher.FaceMatchError):
                matcher.read_image(path)

    def test_chinese_path_can_be_read(self):
        image = np.zeros((32, 32, 3), dtype=np.uint8)
        ok, data = matcher.cv2.imencode(".png", image)
        self.assertTrue(ok)
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "中文 图片.png"
            path.write_bytes(data.tobytes())
            loaded = matcher.read_image(path)
            self.assertEqual(loaded.shape[:2], (32, 32))


@unittest.skipUnless(
    os.environ.get("FACE_MATCH_TEST_IMAGES"),
    "设置 FACE_MATCH_TEST_IMAGES=图1;图2 后运行真实模型集成测试",
)
class IntegrationTests(unittest.TestCase):
    def test_real_pair(self):
        path1, path2 = os.environ["FACE_MATCH_TEST_IMAGES"].split(";", 1)
        result = matcher.match_faces(path1, path2)
        self.assertIsNotNone(result.similarity)


if __name__ == "__main__":
    unittest.main()
