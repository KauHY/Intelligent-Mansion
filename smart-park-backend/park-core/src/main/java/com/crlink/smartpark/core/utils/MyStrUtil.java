package com.crlink.smartpark.core.utils;


import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Random;

public class MyStrUtil {

    private static final int FILL_LENGTH = 8;
    private static final char FILL_VAL = '0';

    /**
     * 首字符小写
     *
     * @param str
     * @return
     */
    public static String lowerCaseFirstChar(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 8位随机字符串，数字+英文字母
     *
     * @return
     */
    public static String genRandomNum() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    /**
     * 把ICC获取的ID转成No（目前ICC卡片使用）
     *
     * @param id
     * @return     */
    public static String parseIdToNo(Long id) {
        String str = HexUtil.toHex(id);
        if (str.length() >= FILL_LENGTH) {
            return str.toUpperCase();
        }
        return StrUtil.padPre(str, FILL_LENGTH, FILL_VAL).toUpperCase();
    }

}
