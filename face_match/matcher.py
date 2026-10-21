"""PC 端双图人脸匹配：YuNet 检测 + SFace 特征，余弦相似度判定。

只做 1:1 人脸核验，不建立身份库。两张图片都必须能检测到人脸。
模型首次运行时从 OpenCV Zoo 下载到 ``face_match/models/``。
"""
from __future__ import annotations

import os
import sys
import urllib.error
import urllib.request
from dataclasses import dataclass
from pathlib import Path
from typing import Any

import numpy as np

try:
    import cv2
except ImportError:  # 延迟到运行时给出中文安装提示，便于 import 本模块
    cv2 = None

YUNET_NAME = "face_detection_yunet_2023mar.onnx"
SFACE_NAME = "face_recognition_sface_2021dec.onnx"
MODEL_DIR = Path(__file__).resolve().parent / "models"

# OpenCV Zoo 对 SFace 余弦相似度给出的推荐阈值。
DEFAULT_THRESHOLD = 0.363
# 采集图只有 320x240，检测阈值适当放宽。
DEFAULT_DET_THRESHOLD = 0.6

# (显示名, 文件名, 下载地址, 最小有效字节数)
_MODEL_SPECS = (
    (
        "YuNet 人脸检测",
        YUNET_NAME,
        "https://github.com/opencv/opencv_zoo/raw/main/models/"
        "face_detection_yunet/" + YUNET_NAME,
        200 * 1024,
    ),
    (
        "SFace 人脸特征",
        SFACE_NAME,
        "https://github.com/opencv/opencv_zoo/raw/main/models/"
        "face_recognition_sface/" + SFACE_NAME,
        30 * 1024 * 1024,
    ),
)

# cv2.FaceRecognizerSF_FR_COSINE 在 OpenCV 4.x 中为 0；取不到时用 0 兜底。
_COSINE = getattr(cv2, "FaceRecognizerSF_FR_COSINE", 0) if cv2 is not None else 0

# (检测模型绝对路径, 特征模型绝对路径, 检测阈值) -> (detector, recognizer)
_MODEL_CACHE: dict[tuple[str, str, float], tuple[Any, Any]] = {}


class FaceMatchError(RuntimeError):
    """图片不可读、模型缺失、下载或推理失败。"""


@dataclass(frozen=True)
class MatchResult:
    success: bool
    similarity: float | None
    threshold: float
    reason: str = ""
    face_count1: int = 0
    face_count2: int = 0
    image_size1: tuple[int, int] | None = None
    image_size2: tuple[int, int] | None = None
    note: str = ""


def _require_cv2() -> None:
    if cv2 is None:
        raise FaceMatchError(
            "未安装 opencv-python。请运行："
            "python -m pip install -r face_match/requirements.txt"
        )
    if not hasattr(cv2, "FaceDetectorYN") or not hasattr(cv2, "FaceRecognizerSF"):
        raise FaceMatchError(
            f"当前 OpenCV {getattr(cv2, '__version__', '未知')} 不支持 "
            "FaceDetectorYN/FaceRecognizerSF，请安装 opencv-python>=4.10,<5。"
        )


def _download(label: str, url: str, path: Path, min_size: int, timeout: float) -> None:
    """下载到 .part，校验大小后原子替换到目标文件。"""
    path.parent.mkdir(parents=True, exist_ok=True)
    part = path.with_name(path.name + ".part")
    print(f"[下载模型] {label}: {url}", file=sys.stderr, flush=True)
    try:
        with urllib.request.urlopen(url, timeout=timeout) as response:
            total = int(response.headers.get("Content-Length") or 0)
            received = 0
            next_report = 4 * 1024 * 1024
            with part.open("wb") as out:
                while True:
                    block = response.read(1024 * 1024)
                    if not block:
                        break
                    out.write(block)
                    received += len(block)
                    if received >= next_report or (total and received >= total):
                        print(
                            f"[下载模型] {label}: {received}/"
                            f"{total or '未知'} bytes",
                            file=sys.stderr,
                            flush=True,
                        )
                        next_report += 4 * 1024 * 1024
                out.flush()
                os.fsync(out.fileno())
        size = part.stat().st_size
        if size < min_size:
            raise FaceMatchError(
                f"{label} 模型下载不完整（{size} 字节，预期至少 {min_size} 字节）。"
                f"可能是网络错误页，请检查网络后重试：{url}"
            )
        os.replace(part, path)
        print(f"[下载模型] {label}: 已保存到 {path}", file=sys.stderr, flush=True)
    except FaceMatchError:
        raise
    except (urllib.error.URLError, TimeoutError, OSError) as exc:
        raise FaceMatchError(
            f"下载 {label} 模型失败：{exc}。可手动下载 {url} 并保存为 {path}。"
        ) from exc
    finally:
        part.unlink(missing_ok=True)


def ensure_models(
    model_dir: Path | str = MODEL_DIR,
    allow_download: bool = True,
    timeout: float = 60,
) -> tuple[Path, Path]:
    """返回 (YuNet 路径, SFace 路径)，缺失时按需下载。"""
    model_dir = Path(model_dir)
    resolved: list[Path] = []
    for label, name, url, min_size in _MODEL_SPECS:
        path = model_dir / name
        if path.is_file() and path.stat().st_size >= min_size:
            resolved.append(path)
            continue
        if not allow_download:
            detail = "文件不存在" if not path.exists() else f"文件只有 {path.stat().st_size} 字节"
            raise FaceMatchError(
                f"缺少 {label} 模型：{path}（{detail}）。"
                f"请联网后重新运行，或手动下载 {url} 并保存为 {path}，"
                "然后加上 --no-download 运行。"
            )
        _download(label, url, path, min_size, timeout)
        resolved.append(path)
    return resolved[0], resolved[1]


def read_image(path: Path | str) -> np.ndarray:
    """读取图片为 BGR 数组；使用 imdecode 兼容 Windows 中文路径。"""
    path = Path(path)
    if not path.is_file():
        raise FaceMatchError(f"图片不存在：{path}")
    _require_cv2()
    try:
        data = np.fromfile(str(path), dtype=np.uint8)
    except (OSError, ValueError) as exc:
        raise FaceMatchError(f"读取图片失败：{path}：{exc}") from exc
    image = cv2.imdecode(data, cv2.IMREAD_COLOR)
    if image is None:
        raise FaceMatchError(f"无法解码图片：{path}（支持 JPG/PNG/BMP 等常见格式）")
    return image


def _load_models(
    model_dir: Path | str,
    allow_download: bool,
    det_threshold: float,
    download_timeout: float = 60,
) -> tuple[Any, Any]:
    _require_cv2()
    yunet, sface = ensure_models(model_dir, allow_download, download_timeout)
    key = (str(yunet.resolve()), str(sface.resolve()), float(det_threshold))
    cached = _MODEL_CACHE.get(key)
    if cached is not None:
        return cached
    try:
        detector = cv2.FaceDetectorYN.create(
            str(yunet), "", (320, 320), float(det_threshold), 0.3, 5000
        )
        recognizer = cv2.FaceRecognizerSF.create(str(sface), "")
    except cv2.error as exc:
        raise FaceMatchError(
            f"模型加载失败：{exc}。请删除 {Path(model_dir)} 下的模型文件后重新下载，"
            "或升级 opencv-python（需要 >=4.10,<5）。"
        ) from exc
    _MODEL_CACHE[key] = (detector, recognizer)
    return detector, recognizer


def detect_largest_face(detector: Any, image: np.ndarray) -> tuple[np.ndarray | None, int]:
    """返回面积最大的人脸行（15 个浮点数）和检测到的人脸数。"""
    height, width = image.shape[:2]
    detector.setInputSize((width, height))
    _count, faces = detector.detect(image)
    if faces is None or len(faces) == 0:
        return None, 0
    if len(faces) == 1:
        return faces[0], 1
    areas = [float(face[2]) * float(face[3]) for face in faces]
    return faces[int(np.argmax(areas))], len(faces)


def _extract_feature(recognizer: Any, image: np.ndarray, face: np.ndarray) -> np.ndarray:
    try:
        aligned = recognizer.alignCrop(image, face)
        return recognizer.feature(aligned)
    except cv2.error as exc:
        raise FaceMatchError(f"人脸对齐或特征提取失败：{exc}") from exc


def match_faces(
    path1: Path | str,
    path2: Path | str,
    *,
    threshold: float = DEFAULT_THRESHOLD,
    model_dir: Path | str = MODEL_DIR,
    allow_download: bool = True,
    det_threshold: float = DEFAULT_DET_THRESHOLD,
) -> MatchResult:
    """比较两张图片是否为同一人。

    - 正常比对（包括未检测到人脸）返回 MatchResult；
    - 图片不可读、模型缺失、下载或推理失败抛出 FaceMatchError。
    """
    threshold = float(threshold)
    det_threshold = float(det_threshold)
    if not 0.0 <= threshold <= 1.0:
        raise ValueError("threshold 必须在 0 到 1 之间")
    if not 0.0 < det_threshold <= 1.0:
        raise ValueError("det_threshold 必须在 (0, 1] 之间")

    detector, recognizer = _load_models(model_dir, allow_download, det_threshold)
    image1 = read_image(path1)
    image2 = read_image(path2)
    face1, count1 = detect_largest_face(detector, image1)
    face2, count2 = detect_largest_face(detector, image2)
    size1 = (int(image1.shape[1]), int(image1.shape[0]))
    size2 = (int(image2.shape[1]), int(image2.shape[0]))

    if face1 is None or face2 is None:
        missing = []
        if face1 is None:
            missing.append("第 1 张")
        if face2 is None:
            missing.append("第 2 张")
        return MatchResult(
            success=False,
            similarity=None,
            threshold=threshold,
            reason=f"{'、'.join(missing)}图片未检测到人脸",
            face_count1=count1,
            face_count2=count2,
            image_size1=size1,
            image_size2=size2,
        )

    feature1 = _extract_feature(recognizer, image1, face1)
    feature2 = _extract_feature(recognizer, image2, face2)
    try:
        similarity = float(recognizer.match(feature1, feature2, _COSINE))
    except cv2.error as exc:
        raise FaceMatchError(f"特征比对失败：{exc}") from exc

    success = similarity >= threshold
    note = ""
    if count1 > 1 or count2 > 1:
        note = f"已选择面积最大的人脸（两张图分别检测到 {count1}/{count2} 张人脸）"
    return MatchResult(
        success=success,
        similarity=similarity,
        threshold=threshold,
        reason="" if success else "相似度低于阈值",
        face_count1=count1,
        face_count2=count2,
        image_size1=size1,
        image_size2=size2,
        note=note,
    )
