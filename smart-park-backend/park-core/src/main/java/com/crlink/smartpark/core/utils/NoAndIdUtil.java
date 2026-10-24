package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** * @desc
 */
public class NoAndIdUtil {

    public static String generateCardNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHSSS"));
        return "C" + timestamp + RandomUtil.randomNumbers(3);
    }

    public static Long generateDevicePersonId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
        return Long.parseLong(timestamp + RandomUtil.randomNumbers(1));
    }

    /**
     * 生成唯一编码
     *
     * @return
     */
    public static String generateCode() {
        return String.valueOf(cn.hutool.core.util.IdUtil.getSnowflake(1, 1).nextId());
    }

}
