"""Host C harness for actual firmware bridge header; not a cross build."""
import os
from pathlib import Path
import subprocess
import sys

def main():
    root = Path(__file__).resolve().parents[1]
    out = root / 'tmp/photo_bridge'
    out.mkdir(parents=True, exist_ok=True)
    firmware = root / 'XiUOS/App_Framework/Framework/knowing/kpu/k210_yolov2_detect_procedure'
    (out / 'shell.h').write_text('#define SHELL_EXPORT_CMD(...)\n')
    detector = (firmware / 'k210_yolov2_detect.c').read_text(encoding='utf-8')
    crc = detector[detector.index('static uint32_t model_crc32'):detector.index('#include "photo_bridge.h"')]
    source = r'''
    #include <stdio.h>
    #include <stdint.h>
    #include <stdlib.h>
    #include <string.h>
    #include <assert.h>
    #define ADD_XIZI_FEATURES 1
    #define BSP_USING_LCD 1
    #define LCD_SIZE 320
    #define LCD_DOT_TYPE 1
    #define NULL_PARAMETER 0
    typedef int pthread_mutex_t;
    typedef struct {int type; struct {uint16_t *pixel_color; int x_startpos,x_endpos,y_startpos,y_endpos;}pixel_info;} LcdWriteParam;
    static int locked, writes, two_seconds;
    static int PrivMutexCreate(int *m, void *a) { (void)m; (void)a; return 0; }
    static int PrivMutexObtain(int *m) { (void)m; assert(!locked); locked=1; return 0; }
    static int PrivMutexAbandon(int *m) { (void)m; assert(locked); locked=0; return 0; }
    static void PrivTaskDelay(int ms) { assert(!locked); if(ms==2000)two_seconds++; }
    static int PrivWrite(int fd, LcdWriteParam *p,int n) {
     (void)fd;(void)n;assert(!locked);assert(p->pixel_info.y_startpos>=280 && p->pixel_info.y_endpos<320);writes++;return 0;
    }
    static int image_width=2,image_height=2;
    static unsigned char pixels[]={0,248,224,7,31,0,255,255};
    static unsigned char *showbuffer=pixels;
    '''
    source += crc + '\n#include "photo_bridge.h"\n'
    source += r'''
    int main(void) {
     char *capture[]={"photo_capture","123"}; char *wrong[]={"photo_saved","124"};
     photo_init(); assert(photo_ready);
     photo_on_detection(1); assert(photo_state==0); /* serial mode is PC-triggered */
     photo_saved(2,capture); assert(photo_state==0);
     photo_capture(2,capture); assert(photo_state==1);
     photo_send_frame(); assert(photo_state==3);
     photo_saved(2,wrong);photo_show_saved(1);assert(writes==0);
     photo_saved(2,capture);assert(photo_state==4);photo_show_saved(1);
     assert(writes==80 && two_seconds==1 && photo_state==0);
     photo_saved(2,capture);photo_show_saved(1);assert(two_seconds==1);
     photo_capture(2,capture);photo_send_frame();assert(photo_state==3);
     photo_capture(2,wrong);assert(photo_state==1 && photo_id==124);
     fprintf(stderr,"PASS firmware: framing, CRC, matching ACK, stale/duplicate ACK, LCD bounds, 2000ms delay, recovery\n");
     return 0;
    }
    '''
    (out / 'firmware_test.c').write_text(source, encoding='utf-8')
    compiler = sys.argv[1] if len(sys.argv) > 1 else 'gcc'
    env = dict(os.environ)
    env['PATH'] = str(Path(compiler).parent) + os.pathsep + env.get('PATH', '')
    subprocess.run([compiler, '-std=c99', '-Wall', '-Wextra', '-Werror', '-I' + str(out),
                    '-I' + str(firmware), str(out / 'firmware_test.c'), '-o', str(out / 'firmware_test.exe')], check=True, env=env)
    result = subprocess.run([str(out / 'firmware_test.exe')], check=True, capture_output=True, env=env)
    print(result.stderr.decode())
    # Feed actual C-emitted records to the Python receiver.
    from bridge import Bridge
    sent = []
    receiver = Bridge(sent.append, out / 'received')
    receiver.pending = 123
    receiver.deadline = float('inf')
    receiver.feed(result.stdout)
    assert sent == [b'photo_saved 123\r'], sent
    print('PASS C firmware output -> Python receiver -> PNG -> matching ACK')

    # Compile the actual create/bind/start helper with a deterministic API stub.
    start = detector.index('static int start_detect_on_cpu0(')
    stop = detector.index('\n#endif', start)
    task_source = r'''
    #include <assert.h>
    #include <stdio.h>
    #include <string.h>
    #define STACK_SIZE (128 * 1024)
    #define THREAD_PRIORITY_D 11
    typedef long pthread_t;
    typedef struct {char name[32]; void *func_entry; void *func_param; int stack_size; unsigned char prio;} UtaskType;
    static pthread_t tid;
    static int stage, fail_stage, deleted, test_parameter;
    static void *thread_detect_entry(void *p) {return p;}
    static int UserTaskCreate(UtaskType t) {
        assert(stage++ == 0 && t.func_param == &test_parameter);
        assert(t.stack_size == STACK_SIZE && t.prio == 11 && !strcmp(t.name,"face_det"));
        return fail_stage == 1 ? -1 : 0; /* zero is a valid task ID */
    }
    static int UserTaskCoreCombine(int id, int core) {
        assert(stage++ == 1 && id == 0 && core == 0);
        return fail_stage == 2 ? -2 : 0;
    }
    static int UserTaskStartup(int id) {
        assert(stage++ == 2 && id == 0);
        return fail_stage == 3 ? -3 : 0;
    }
    static void UserTaskDelete(int id) {assert(id == 0);deleted++;}
    ''' + detector[start:stop] + r'''
    int main(void) {
        for(fail_stage=0;fail_stage<=3;fail_stage++) {
            stage=deleted=0;tid=-1;
            int result=start_detect_on_cpu0(&test_parameter);
            if(!fail_stage) {assert(result==0 && tid==0 && stage==3 && deleted==0);}
            else {assert(result == -fail_stage && tid==-1 && stage==fail_stage);
                  assert(deleted==(fail_stage>1));}
        }
        puts("PASS task start: CPU binding precedes startup; create/bind/start failures clean up correctly");
        return 0;
    }
    '''
    (out / 'task_start_test.c').write_text(task_source, encoding='utf-8')
    subprocess.run([compiler, '-std=c99', '-Wall', '-Wextra', '-Werror',
                    str(out / 'task_start_test.c'), '-o', str(out / 'task_start_test.exe')], check=True, env=env)
    subprocess.run([str(out / 'task_start_test.exe')], check=True, env=env)


if __name__ == '__main__':
    main()
