"""PC 端双图人脸匹配模块。"""
from .matcher import (
    DEFAULT_DET_THRESHOLD,
    DEFAULT_THRESHOLD,
    MODEL_DIR,
    FaceMatchError,
    MatchResult,
    ensure_models,
    extract_face_feature,
    match_photo_to_feature,
    match_faces,
)

__all__ = [
    "DEFAULT_DET_THRESHOLD",
    "DEFAULT_THRESHOLD",
    "MODEL_DIR",
    "FaceMatchError",
    "MatchResult",
    "ensure_models",
    "extract_face_feature",
    "match_photo_to_feature",
    "match_faces",
]
