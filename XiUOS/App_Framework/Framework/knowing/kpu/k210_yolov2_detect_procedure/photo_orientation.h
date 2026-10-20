/* Correct the camera mounting before inference and capture, after DVP is idle.
 * The LCD mounting is configured separately in the board LCD driver. */
#ifndef PHOTO_ORIENTATION_H
#define PHOTO_ORIENTATION_H
#ifndef PHOTO_CAMERA_ROTATE_180
#define PHOTO_CAMERA_ROTATE_180 1
#endif
static void photo_reverse_pixels(unsigned char *data, size_t pixels, size_t bytes)
{
    for (size_t i=0;i<pixels/2;i++) {
        size_t j=pixels-1-i;
        for (size_t k=0;k<bytes;k++) {
            unsigned char tmp=data[i*bytes+k];
            data[i*bytes+k]=data[j*bytes+k];
            data[j*bytes+k]=tmp;
        }
    }
}
static void photo_orient_frame(unsigned char *display, unsigned char *rgb,
                              size_t width, size_t height, size_t plane_size)
{
#if PHOTO_CAMERA_ROTATE_180
    size_t pixels=width*height;
    /* XiZi places camera data at the beginning of each planar RGB channel.
     * Leave any model padding untouched. */
    if (pixels>plane_size) return;
    photo_reverse_pixels(display,pixels,2);
    for (size_t channel=0;channel<3;channel++)
        photo_reverse_pixels(rgb+channel*plane_size,pixels,1);
#else
    (void)display; (void)rgb; (void)width; (void)height; (void)plane_size;
#endif
}
#endif
