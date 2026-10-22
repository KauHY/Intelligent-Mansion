package com.crlink.smartpark.core.utils;

import cn.hutool.captcha.CaptchaUtil;

/**
 * @ClassName MyCaptchaUtil
 * @description:
 **/
public class MyCaptchaUtil extends CaptchaUtil {

    public static MyLineCaptcha createMyLineCaptcha(int width, int height, int codeCount, int lineCount) {
        return new MyLineCaptcha(width, height, codeCount, lineCount);
    }

}
