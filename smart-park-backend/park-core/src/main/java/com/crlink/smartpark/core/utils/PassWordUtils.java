package com.crlink.smartpark.core.utils;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 * */
public class PassWordUtils {

    public static final String NUMBER = "number";
    public static final String LETTER = "letter";
    public static final String SYMBOL = "symbol";
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 32;

    private static PassWordUtils instance;

    public PassWordUtils() {
        instance = this;
    }

    /**
     * 获取当前对象
     */
    public static PassWordUtils getInstance() {
        return instance;
    }


    /**
     * 根据条件获得复杂密码
     *
     * @param length             密码的位数 8-32位
     * @param includeNumber      是否包含数字，默认为true
     * @param includeUpperLetter 是否包含大写字母，默认为true
     * @param includeSymbol      是否包含特殊字符，默认为true
     *                           默认包含小写字母，没有条件时，将全部使用小写字母
     * @return 生成的密码
     */
    public String getPassword(int length, boolean includeNumber, boolean includeUpperLetter, boolean includeSymbol) {
        if (Objects.isNull(length) && (length <= MIN_LENGTH || length >= MAX_LENGTH)) {
            throw new Error("生成的密码的位数为8-32位");
        }
        String[] passWord = new String[length];
        // 密码的第一位为字母
        if (includeNumber) {
            passWord[0] = String.valueOf(this.getRandomNum());
        }
        // 生成从第二位开始的密码
        for (int i = 1; i < length; i++) {
            String funcIndex = this.getFormIndex();
            // funcIndex=number时候，调用生成数字的方法；=letter时，生成大写字母；=symbol时，生成特殊字符。
            if (NUMBER.equals(funcIndex) && includeNumber) {
                passWord[i] = String.valueOf(this.getRandomNum());
            } else if (LETTER.equals(funcIndex) && includeUpperLetter) {
                passWord[i] = this.getRandomLetters();
            } else if (SYMBOL.equals(funcIndex) && includeSymbol) {
                passWord[i] = this.getSymbols();
            } else {
                // 当不满足生成的条件时，生成小写字母
                passWord[i] = this.getRandomLetters().toLowerCase();
            }
        }
        int index = (int) (Math.random() * (length - 1) + 1);
        if (includeUpperLetter) {
            passWord[index] = this.getRandomLetters().toUpperCase();
        } else {
            passWord[index] = this.getRandomLetters().toLowerCase();
        }
        String result = "";
        for (String t : passWord) {
            result += t;
        }
        return result;
    }

    private int getRandomNum() {
        int num = (int) (Math.random() * 10);
        return num;
    }

    private String getRandomLetters() {
        String[] letters = {
                "L", "W", "E", "R", "B", "Y", "U", "I", "G",
                "P", "Q", "S", "C", "F", "O", "H", "J", "K",
                "A", "Z", "X", "D", "V", "T", "N", "M"
        };
        int strIndex = (int) (Math.random() * 26);
        int strUpper = ((int) (Math.random() * 100)) % 2;
        if (strUpper != 1) {
            return letters[strIndex].toLowerCase();
        }
        return letters[strIndex];
    }

    private String getSymbols() {
        String[] symbols = {"!", "_", "@", "#", "$", "&", "*"};
        int index = (int) (Math.random() * 7);
        return symbols[index];
    }

    private String getFormIndex() {
        int formIndex = (int) (Math.random() * 18 + 1);
        if ((formIndex % 3) == 0) {
            return SYMBOL;
        } else if ((formIndex % 5) == 1) {
            return LETTER;
        } else {
            return NUMBER;
        }
    }
}
