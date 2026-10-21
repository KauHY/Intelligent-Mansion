# 人脸模型目录

`face_match` 首次运行时会自动把以下模型下载到本目录。模型文件不提交到 git。

| 文件 | 用途 | 大小 | 下载地址 |
| --- | --- | --- | --- |
| `face_detection_yunet_2023mar.onnx` | YuNet 人脸检测 | 232,589 B | <https://github.com/opencv/opencv_zoo/raw/main/models/face_detection_yunet/face_detection_yunet_2023mar.onnx> |
| `face_recognition_sface_2021dec.onnx` | SFace 人脸特征（128 维） | 38,696,353 B | <https://github.com/opencv/opencv_zoo/raw/main/models/face_recognition_sface/face_recognition_sface_2021dec.onnx> |

离线部署时手动下载并保持上面的文件名，然后运行：

```powershell
python -m face_match 图片1.png 图片2.png --no-download
```

如果文件明显小于上表大小，通常是下载到了 GitHub 的错误页面，请删除后重新下载。
