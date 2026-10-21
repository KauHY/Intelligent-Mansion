"""命令行入口：python -m face_match 图片1 图片2。

标准输出只有一行 success 或 failed；诊断信息写入标准错误。
退出码：0 匹配成功，1 匹配失败，2 参数/文件/模型错误。
"""
from __future__ import annotations

import argparse
import sys
from pathlib import Path

from .matcher import (
    DEFAULT_DET_THRESHOLD,
    DEFAULT_THRESHOLD,
    MODEL_DIR,
    FaceMatchError,
    match_faces,
)


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        prog="python -m face_match",
        description="比较两张图片是否为同一人，输出 success 或 failed。",
    )
    parser.add_argument("image1", type=Path, help="第一张人脸图片（JPG/PNG/BMP）")
    parser.add_argument("image2", type=Path, help="第二张人脸图片（JPG/PNG/BMP）")
    parser.add_argument(
        "--threshold",
        type=float,
        default=DEFAULT_THRESHOLD,
        help=f"余弦相似度阈值，0 到 1（默认 {DEFAULT_THRESHOLD}）",
    )
    parser.add_argument(
        "--det-threshold",
        type=float,
        default=DEFAULT_DET_THRESHOLD,
        help=f"人脸检测置信度阈值，0 到 1（默认 {DEFAULT_DET_THRESHOLD}）",
    )
    parser.add_argument(
        "--model-dir",
        type=Path,
        default=MODEL_DIR,
        help=f"模型目录（默认 {MODEL_DIR}）",
    )
    parser.add_argument(
        "--no-download",
        action="store_true",
        help="不联网下载模型，只使用模型目录中已有的文件",
    )
    parser.add_argument(
        "-q", "--quiet", action="store_true", help="不在标准错误输出检测细节"
    )
    return parser


def _size_text(size: tuple[int, int] | None) -> str:
    return f"{size[0]}x{size[1]}" if size else "未知"


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    if not 0.0 <= args.threshold <= 1.0:
        parser.error("--threshold 必须在 0 到 1 之间")
    if not 0.0 < args.det_threshold <= 1.0:
        parser.error("--det-threshold 必须在 (0, 1] 之间")

    try:
        result = match_faces(
            args.image1,
            args.image2,
            threshold=args.threshold,
            model_dir=args.model_dir,
            allow_download=not args.no_download,
            det_threshold=args.det_threshold,
        )
    except (FaceMatchError, ValueError) as exc:
        print(f"[人脸匹配] {exc}", file=sys.stderr)
        print("failed")
        return 2

    if not args.quiet:
        print(
            f"[图片1] {args.image1} 尺寸={_size_text(result.image_size1)} "
            f"人脸数={result.face_count1}",
            file=sys.stderr,
        )
        print(
            f"[图片2] {args.image2} 尺寸={_size_text(result.image_size2)} "
            f"人脸数={result.face_count2}",
            file=sys.stderr,
        )
        if result.similarity is not None:
            print(
                f"[相似度] {result.similarity:.6f}（阈值 {result.threshold:g}）",
                file=sys.stderr,
            )
        if result.note:
            print(f"[提示] {result.note}", file=sys.stderr)
        if not result.success:
            print(f"[原因] {result.reason}", file=sys.stderr)

    print("success" if result.success else "failed")
    return 0 if result.success else 1


if __name__ == "__main__":
    sys.exit(main())
