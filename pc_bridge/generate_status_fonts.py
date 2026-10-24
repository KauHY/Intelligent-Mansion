"""Development helper; needs Pillow and Microsoft YaHei on Windows.
Generates the check-in status toast bitmaps keyed by the PC result JSON
status code (docs 09 §7.4). The default captured-toast artwork stays in
photo_toast_font.h; this header only adds the six status phrases, so the
board never needs a dynamic CJK font. The generated header is checked in;
neither PC receiver nor board needs Pillow.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

root = Path(__file__).resolve().parents[1]
phrases = [
    (1, '签到成功'),
    (2, '已签到'),
    (3, '非与会人员'),
    (4, '未识别'),
    (5, '未在签到时段'),
    (7, '处理失败'),
]
font = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 24)

decls = []
previews = []
for status, text in phrases:
    width = len(text) * 24
    row_bytes = (width + 7) // 8
    image = Image.new('1', (width, 28))
    draw = ImageDraw.Draw(image)
    draw.text((0, -4), text, font=font, fill=1)
    body = []
    for y in range(image.height):
        row = []
        for x in range(0, image.width, 8):
            row.append(sum((1 << (7 - bit)) for bit in range(8) if image.getpixel((x + bit, y))))
        body.append('    {' + ','.join(f'0x{v:02x}' for v in row) + '}')
    decls.append(f'static const unsigned char photo_font_status{status}[28][{row_bytes}] = {{\n'
                 + ',\n'.join(body) + '\n};')
    previews.append(image)

names = ',\n    '.join(f'{{photo_font_status{s}[0], {len(t) * 24}, 28}}' for s, t in phrases)
header = (
    '/* Generated monochrome artwork: check-in status toasts keyed by the PC\n'
    ' * result JSON status code (docs 09 §7.4). Bitmaps share the 28-row height\n'
    ' * of photo_toast_font.h, whose default captured toast is index 0. */\n'
    '#ifndef PHOTO_STATUS_FONT_H\n'
    '#define PHOTO_STATUS_FONT_H\n'
    '/* photo_toast_font.h keeps its own guard; include it for the default art. */\n'
    '#include "photo_toast_font.h"\n'
    'typedef struct { const unsigned char *bits; unsigned char w, h; } photo_bitmap_t;\n'
    + '\n'.join(decls) + '\n'
    'static const photo_bitmap_t photo_status_fonts[] = {\n'
    '    /* 0 default (JSON unreadable or unknown status): photo captured */\n'
    '    {(const unsigned char *)photo_font, PHOTO_FONT_W, PHOTO_FONT_H},\n'
    '    ' + names + ',\n'
    '};\n'
    '/* Map a check-in status code to its toast; unknown codes keep the default. */\n'
    'static int photo_status_bitmap(int status)\n'
    '{\n'
    '    switch (status) {\n'
    '    case 1: return 1;\n'
    '    case 2: return 2;\n'
    '    case 3: return 3;\n'
    '    case 4: return 4;\n'
    '    case 5: return 5;\n'
    '    case 7: return 6;\n'
    '    default: return 0;\n'
    '    }\n'
    '}\n'
    '#endif\n'
)
target = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure/photo_status_font.h'
target.write_text(header, encoding='utf-8')
preview = Image.new('RGB', (320, 40 * len(phrases)), 'black')
for i, image in enumerate(previews):
    single = Image.new('RGB', (320, 40), 'black')
    single.paste(image.convert('RGB'), ((320 - image.width) // 2, 6))
    preview.paste(single, (0, i * 40))
(root / 'tmp/photo_bridge').mkdir(parents=True, exist_ok=True)
preview.save(root / 'tmp/photo_bridge/status_toasts.png')
print('generated', target)
