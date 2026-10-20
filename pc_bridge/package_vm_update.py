"""Create a source-only VM update; never include local Wi-Fi credentials."""
from pathlib import Path
from zipfile import ZipFile, ZIP_DEFLATED
import hashlib

root = Path(__file__).resolve().parents[1]
detector = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure'
names = ('Kconfig', 'Makefile', 'k210_yolov2_detect.c', 'photo_bridge.h',
         'photo_wifi.h', 'photo_wifi_config.example.h', 'photo_toast_font.h',
         'face_model.c', 'face_model.h')
entries = {'detector/' + name: (detector / name).read_bytes() for name in names}
entries['Applications/main.c'] = (root/'XiUOS/App_Framework/Applications/main.c').read_bytes()
entries['SEND_FIX.md'] = (root/'pc_bridge/SEND_FIX.md').read_bytes()
entries['TIMEOUT_FIX.md'] = (root/'pc_bridge/TIMEOUT_FIX.md').read_bytes()
entries['Windows/tcp_bridge.py'] = (root/'pc_bridge/tcp_bridge.py').read_bytes()
entries['JOIN_FIX.md'] = (root/'pc_bridge/JOIN_FIX.md').read_bytes()
entries['PIN_FIX.md'] = (root/'pc_bridge/PIN_FIX.md').read_bytes()
entries['README_VM.md'] = (root/'pc_bridge/UPDATE_BUILTIN_WIFI.md').read_bytes()
entries['WIFI_SETUP.md'] = (root/'pc_bridge/WIFI_SETUP.md').read_bytes()
entries['SHA256SUMS.txt'] = ''.join(
    f'{hashlib.sha256(data).hexdigest()}  {name}\n'
    for name, data in entries.items()).encode('ascii')
target = root/'output/XiUOS-WiFi-Builtin-VM-v6.zip'
target.parent.mkdir(exist_ok=True)
prefix = 'XiUOS-WiFi-Builtin-VM-v6/'
with ZipFile(target, 'w', ZIP_DEFLATED) as archive:
    for name, data in entries.items():
        archive.writestr(prefix+name, data)
with ZipFile(target) as archive:
    assert archive.testzip() is None
    assert len(archive.namelist()) == len(entries)
    for name, data in entries.items():
        assert archive.read(prefix+name) == data
print(f'Verified {len(entries)} files: {target} ({target.stat().st_size} bytes)')
