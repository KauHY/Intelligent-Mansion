#include <stdio.h>
#include <transform.h>
extern int FrameworkInit(void);
extern void ApplicationOtaTaskInit(void);
#ifdef USING_K210_YOLOV2_DETECT
extern void k210_detect(char *json_file_path);
#endif
int main(void)
{
    FrameworkInit();
#ifdef APPLICATION_OTA
    ApplicationOtaTaskInit();
#endif
#if defined(USING_K210_YOLOV2_DETECT) && defined(K210_MODEL_BUILTIN)
    printf("[lab5.5] builtin FACE detection; USB not required\n");
    k210_detect(NULL);
#elif defined(USING_K210_YOLOV2_DETECT) && defined(MOUNT_USB)
    printf("[lab5.5] USB face detection: /FACE.JSN + /FACE.KMD\n");
    k210_detect("/FACE.JSN");
#else
    printf("[lab5.5] Enable USING_K210_YOLOV2_DETECT and MOUNT_USB.\n");
#endif
    return 0;
}
