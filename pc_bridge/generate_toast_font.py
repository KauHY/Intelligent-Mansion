"""Generate the fixed XiUOS LCD attendance-result glyphs.

The K210 image does not carry a general-purpose Chinese font.  These small,
pre-rendered phrases keep result feedback deterministic without adding a font
engine or depending on the PC at display time.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

root = Path(__file__).resolve().parents[1]
texts = (
    '人像已完成采集',  # JSON unavailable: the image was nevertheless ACKed
    '签到成功',
    '已签到',
    '无权限',
    '未识别',
    '签到未开始',
    '签到已结束',
    '暂不可用',
    '识别失败',
)
font = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 24)
height, width = 28, 168
art = []
for text in texts:
    image = Image.new('1', (width, height))
    ImageDraw.Draw(image).text((0, -4), text, font=font, fill=1)
    art.append([
        [sum((1 << (7 - bit)) for bit in range(8)
             if image.getpixel((x + bit, y)))
         for x in range(0, width, 8)]
        for y in range(height)
    ])
header = ('/* Generated fixed Chinese attendance-result artwork. */\n'
          '#define PHOTO_FONT_W 168\n#define PHOTO_FONT_H 28\n'
          '#define PHOTO_TOAST_CAPTURED 0\n#define PHOTO_TOAST_SUCCESS 1\n'
          '#define PHOTO_TOAST_DUPLICATE 2\n#define PHOTO_TOAST_NO_PERMISSION 3\n'
          '#define PHOTO_TOAST_NOT_RECOGNIZED 4\n#define PHOTO_TOAST_NOT_STARTED 5\n'
          '#define PHOTO_TOAST_ENDED 6\n#define PHOTO_TOAST_UNAVAILABLE 7\n'
          '#define PHOTO_TOAST_FAILED 8\n#define PHOTO_TOAST_COUNT 9\n'
          'static const unsigned char photo_toast_font[PHOTO_TOAST_COUNT][PHOTO_FONT_H][PHOTO_FONT_W / 8] = {\n')
for glyph in art:
    header += '    {\n' + ',\n'.join(
        '        {' + ','.join(f'0x{v:02x}' for v in row) + '}' for row in glyph
    ) + '\n    },\n'
header += '};\n'
target = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_toast_font.h'
target.write_text(header, encoding='utf-8')
preview = Image.new('RGB', (320, len(texts) * 40), 'black')
for index, text in enumerate(texts):
    image = Image.new('1', (width, height))
    ImageDraw.Draw(image).text((0, -4), text, font=font, fill=1)
    preview.paste(image.convert('RGB'), (76, index * 40 + 6))
(root / 'tmp/photo_bridge').mkdir(parents=True, exist_ok=True)
preview.save(root / 'tmp/photo_bridge/toast.png')
