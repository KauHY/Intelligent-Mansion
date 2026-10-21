# face_match：PC 端双图人脸匹配

在电脑上判断两张图片是否为同一人。使用 OpenCV DNN 的 **YuNet** 检测并对齐人脸，用 **SFace** 提取 128 维特征，计算余弦相似度后输出：

```text
success
```

或

```text
failed
```

本模块只做 1:1 人脸核验：不建立身份库，不判断“这个人是谁”，也不包含会议权限与考勤逻辑。后续身份识别服务可以复用 `match_faces()` 以及模块内的检测、对齐和特征提取流程。

## 功能与限制

- 两张图片都必须能检测到人脸；无人脸时判为 `failed`。
- 一张图有多张人脸时，自动选择面积最大的人脸，并在诊断信息中提示。
- 普通 RGB 图片无法证明是活体，不能防止纸质照片或屏幕翻拍。
- 开发板采集图只有 320×240，人脸过小或过暗时识别分数会下降，建议现场正对摄像头、靠近一些。

## 安装

需要 Python 3.10 或更新版本：

```powershell
python -m pip install -r face_match/requirements.txt
```

若项目已有 `.venv`：

```powershell
.venv\Scripts\python.exe -m pip install -r face_match\requirements.txt
```

安装后可以检查 OpenCV 版本和所需接口：

```powershell
python -c "import cv2; print(cv2.__version__, hasattr(cv2, 'FaceDetectorYN'), hasattr(cv2, 'FaceRecognizerSF'))"
```

## 快速开始

在项目根目录运行：

```powershell
python -m face_match tmp_photos\face_A.png tmp_photos\face_B.png
```

- **标准输出**只有一行 `success` 或 `failed`，方便脚本调用。
- **标准错误**输出两张图的尺寸、人脸数、相似度、阈值和失败原因。
- 退出码：`0` 匹配成功，`1` 匹配失败，`2` 参数、文件或模型错误。

常用选项：

```powershell
# 同一张图自比，用于确认模型和图片读取正常，相似度应接近 1.0
python -m face_match tmp_photos\face_A.png tmp_photos\face_A.png

# 调整判定阈值
python -m face_match tmp_photos\face_A.png tmp_photos\face_B.png --threshold 0.4

# 人脸较小或画面偏暗时放宽检测阈值
python -m face_match tmp_photos\face_A.png tmp_photos\face_B.png --det-threshold 0.5

# 只输出 success/failed，不显示诊断信息
python -m face_match tmp_photos\face_A.png tmp_photos\face_B.png --quiet
```

在批处理中读取结果：

```bat
python -m face_match tmp_photos\a.png tmp_photos\b.png > result.txt 2>nul
if errorlevel 1 echo 不是同一人或比对失败
```

## Python API

```python
from face_match import match_faces

result = match_faces("tmp_photos/a.png", "tmp_photos/b.png")
print(result.success)     # True / False
print(result.similarity)  # 余弦相似度；无人脸时为 None
print(result.reason)      # 失败原因
```

也可以直接控制模型目录和阈值：

```python
result = match_faces(
    "a.png", "b.png",
    threshold=0.363,
    model_dir="face_match/models",
    allow_download=True,
)
```

- 图片不可读、模型缺失或推理失败会抛出 `FaceMatchError`。
- 无人脸、相似度不足属于正常比对结果，返回 `MatchResult(success=False, ...)`。
- 模型加载会按“模型目录 + 检测阈值”缓存，重复调用不会重复加载或下载。
- YuNet 的 `setInputSize` 会修改检测器状态，不要在多线程中共享同一个匹配器。

## 模型下载与离线部署

首次运行会自动从 OpenCV Zoo 下载两个模型到 `face_match/models/`：

| 文件 | 用途 | 大小 |
| --- | --- | --- |
| `face_detection_yunet_2023mar.onnx` | YuNet 人脸检测 | 232,589 B |
| `face_recognition_sface_2021dec.onnx` | SFace 人脸特征 | 38,696,353 B |

下载地址见 [models/README.md](models/README.md)。模型文件不提交到 git。

离线部署：

1. 手动下载两个 `.onnx` 文件，保持文件名不变；
2. 放到 `face_match/models/`；
3. 运行时加 `--no-download`：

```powershell
python -m face_match a.png b.png --no-download
```

公司网络需要代理时，可先设置 `HTTPS_PROXY` 环境变量再运行。

## 阈值校准

默认阈值 `0.363` 来自 OpenCV Zoo 对 SFace 的推荐值，适用于正常尺寸、清晰的正脸图片。开发板的 320×240 采集图与之存在差异，实际使用前建议校准：

1. 采集 10～20 对同一人的图片，记录 `--quiet` 之外的相似度输出；
2. 采集 10～20 对不同人的图片，记录相似度；
3. 选择落在两组分布之间、且偏向“误把不同人判为同一人”风险较低的位置；
4. 将选定值写入调用方参数或 `--threshold`。

登记照片和现场照片必须使用同一版模型、同一套预处理参数。当前模块不实现“最佳候选与次佳候选差距”的开放集识别逻辑，那属于后续身份识别服务。

## 测试

```powershell
python -m unittest discover -s face_match -p "test_*.py" -v
```

大部分用例使用 mock，无需下载模型。需要真实模型的集成测试：

```powershell
$env:FACE_MATCH_TEST_IMAGES="tmp_photos\a.png;tmp_photos\b.png"
python -m unittest discover -s face_match -p "test_*.py" -v
Remove-Item Env:\FACE_MATCH_TEST_IMAGES
```

## 故障排查

| 现象 | 处理 |
| --- | --- |
| `未安装 opencv-python` | 运行 `python -m pip install -r face_match/requirements.txt` |
| `不支持 FaceDetectorYN/FaceRecognizerSF` | OpenCV 版本过旧，安装 `opencv-python>=4.10,<5` |
| 模型下载失败 | 检查网络或代理，按 [models/README.md](models/README.md) 手动下载 |
| `无法解码图片` | 文件不是图片或已损坏；确认扩展名和内容 |
| `未检测到人脸` | 换更清晰、正脸、未被遮挡的图片；尝试 `--det-threshold 0.5` |
| 两张同一人的图判为 `failed` | 查看相似度；低分辨率/角度差异大时可按上文校准阈值 |
| 人脸上下颠倒或侧躺 | 本模块不自动旋转图片；先在采集端修正方向 |
| `numpy.core.multiarray` 导入错误 | OpenCV 与 NumPy 版本不匹配，重新安装 `opencv-python>=4.10,<5` |
