"""Compile generated model, verify actual data and parameters against USB sources."""
import json
import os
from pathlib import Path
import struct
import subprocess
import sys
import zlib

root = Path(__file__).resolve().parents[1]
d = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure'
out = root / 'tmp/builtin_model_test'
out.mkdir(parents=True, exist_ok=True)
test = out / 'check.c'
test.write_text(r'''
#include <stdio.h>
#include <stdint.h>
#include <assert.h>
#include "face_model.h"
int main(int argc, char **argv) {
    (void)argc;
    assert(((uintptr_t)face_model & 255)==0);
    assert(face_model_params.kmodel_size==FACE_MODEL_SIZE);
    assert(face_model_params.is_valid==1 && face_model_params.class_num==1);
    FILE *f=fopen(argv[1],"wb"); assert(f);
    assert(fwrite(face_model,1,FACE_MODEL_SIZE,f)==FACE_MODEL_SIZE); fclose(f);
    f=fopen(argv[2],"wb"); assert(f);
    fwrite(&face_model_params,1,sizeof(face_model_params),f); fclose(f);
    return 0;
}
''')
compiler = sys.argv[1] if len(sys.argv)>1 else 'gcc'
env = dict(os.environ)
env['PATH'] = str(Path(compiler).parent) + os.pathsep + env.get('PATH','')
subprocess.run([compiler,'-std=c99','-Wall','-Wextra','-Werror','-I'+str(d),
                '-I'+str(d.parent/'yolov2_json'),str(test),str(d/'face_model.c'),
                '-o',str(out/'check.exe')],check=True,env=env)
subprocess.run([str(out/'check.exe'),str(out/'model.bin'),str(out/'params.bin')],check=True,env=env)
raw = (out/'model.bin').read_bytes()
assert raw == (root/'USB/face.KMD').read_bytes()
assert len(raw)==388776 and zlib.crc32(raw)==0x40429f51
p = json.loads((root/'USB/face.JSN').read_text())
data = (out/'params.bin').read_bytes()
offset=0
for key,fmt in [('anchors','10f'),('net_output_shape','3i'),('net_input_size','2i'),
                ('sensor_output_size','2i'),('kmodel_size','i'),('obj_thresh','20f'),
                ('nms_thresh','f')]:
    vals=struct.unpack_from('<'+fmt,data,offset)
    expected=p[key] if isinstance(p[key],list) else [p[key]]
    assert all(abs(a-b)<1e-5 for a,b in zip(vals,expected)),key
    offset+=struct.calcsize('<'+fmt)
assert data[offset:offset+32].split(b'\0')[0]==b'face'
print('PASS compiled builtin model: exact USB bytes, CRC, 256-byte alignment, all JSON parameters')
