"""Development helper; needs Pillow and Microsoft YaHei on Windows.
Generates just the seven-glyph monochrome phrase, not a redistributable font.
The generated header is checked in; neither PC receiver nor board needs Pillow.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

root = Path(__file__).resolve().parents[1]
text = '人像已完成采集'
font = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 24)
image = Image.new('1', (168, 28))
draw = ImageDraw.Draw(image)
draw.text((0, -4), text, font=font, fill=1)
rows = []
for y in range(image.height):
    row = []
    for x in range(0, image.width, 8):
        row.append(sum((1 << (7 - bit)) for bit in range(8) if image.getpixel((x + bit, y))))
    rows.append('    {' + ','.join(f'0x{v:02x}' for v in row) + '}')
header = ('/* Generated monochrome artwork: 人像已完成采集. */\n'
          '#define PHOTO_FONT_W 168\n#define PHOTO_FONT_H 28\n'
          'static const unsigned char photo_font[28][21] = {\n' + ',\n'.join(rows) + '\n};\n')
target = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_toast_font.h'
target.write_text(header, encoding='utf-8')
preview = Image.new('RGB', (320, 40), 'black')
preview.paste(image.convert('RGB'), (76, 6))
(root / 'tmp/photo_bridge').mkdir(parents=True, exist_ok=True)
preview.save(root / 'tmp/photo_bridge/toast.png')
