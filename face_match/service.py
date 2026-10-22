"""
Face inference microservice for the smart-park Java backend.

Receives JSON requests with base64 images, returns match results.
Listens on a local HTTP port, called by Java via RestTemplate.
"""
import base64
import json
import sys
import tempfile
import time
from pathlib import Path
from http.server import HTTPServer, BaseHTTPRequestHandler

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
sys.path.insert(0, str(Path(__file__).resolve().parent))
try:
    from face_match import encode_feature, extract_face_feature, match_faces, match_photo_to_feature, FaceMatchError
except ImportError:
    from matcher import encode_feature, extract_face_feature, match_faces, match_photo_to_feature, FaceMatchError

PORT = 8901
MODEL_DIR = Path(__file__).resolve().parent / "models"


def log_result(stage: str, result: dict) -> None:
    print(
        f"[{stage}] time={time.strftime('%Y-%m-%d %H:%M:%S')} "
        f"face_count1={result.get('faceCount1', result.get('face_count1', result.get('faceCount', '-')))} "
        f"face_count2={result.get('faceCount2', result.get('face_count2', '-'))} "
        f"similarity={result.get('similarity', '-')} "
        f"success={result.get('success')} reason={result.get('reason') or '-'}",
        flush=True,
    )


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/health":
            self._respond({"status": "ok", "endpoints": ["/match", "/extract", "/match-feature"]})
            return
        self.send_error(404)

    def do_POST(self):
        if self.path not in ("/match", "/extract", "/match-feature"):
            self.send_error(404)
            return
        try:
            length = int(self.headers.get("Content-Length", 0))
            body = json.loads(self.rfile.read(length))
            photo_b64 = body.get("photoBase64", "")
            template_b64 = body.get("templateBase64", "")
            if self.path == "/match-feature":
                template_b64 = body.get("templateFeatureBase64", template_b64)
            if not photo_b64 or (self.path != "/extract" and not template_b64):
                self._respond({"success": False, "similarity": None, "reason": "missing image data"})
                return
            photo_path = self._decode(photo_b64, "photo")
            if self.path == "/extract":
                feature_result = extract_face_feature(photo_path, model_dir=MODEL_DIR, allow_download=False)
                response = {
                    "success": feature_result.success,
                    "reason": feature_result.reason,
                    "faceCount": feature_result.face_count,
                    "imageSize": list(feature_result.image_size or []),
                    "featureBase64": encode_feature(feature_result.feature) if feature_result.success else None,
                }
                log_result("extract", response)
                self._respond(response)
                return
            if self.path == "/match-feature":
                result = match_photo_to_feature(photo_path, template_b64, model_dir=MODEL_DIR, allow_download=False)
                response = {
                    "success": result.success,
                    "similarity": result.similarity,
                    "reason": result.reason,
                    "faceCount1": result.face_count1,
                    "faceCount2": result.face_count2,
                    "note": result.note,
                }
                log_result("match-feature", response)
                self._respond(response)
                return
            template_path = self._decode(template_b64, "template")
            result = match_faces(photo_path, template_path, model_dir=MODEL_DIR, allow_download=False)
            response = {
                "success": result.success,
                "similarity": result.similarity,
                "reason": result.reason,
                "faceCount1": result.face_count1,
                "faceCount2": result.face_count2,
                "note": result.note,
            }
            log_result("match", response)
            self._respond(response)
        except FaceMatchError as exc:
            self._respond({"success": False, "similarity": None, "reason": str(exc)}, status=500)
        except Exception as exc:
            self._respond({"success": False, "similarity": None, "reason": f"internal: {exc}"}, status=500)

    def _decode(self, data: str, name: str) -> str:
        """Strip data URI prefix, decode base64, save to temp file."""
        if "," in data:
            data = data.split(",", 1)[1]
        raw = base64.b64decode(data)
        suffix = ".jpg"
        tmp = tempfile.NamedTemporaryFile(suffix=suffix, prefix=f"fm_{name}_", delete=False)
        tmp.write(raw)
        tmp.close()
        return tmp.name

    def _respond(self, data: dict, status: int = 200):
        body = json.dumps(data).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def log_message(self, format, *args):
        pass  # suppress default stderr logging


def main():
    server = HTTPServer(("0.0.0.0", PORT), Handler)
    print(f"[face-inference] listening on http://0.0.0.0:{PORT}/match", flush=True)
    server.serve_forever()


if __name__ == "__main__":
    main()
