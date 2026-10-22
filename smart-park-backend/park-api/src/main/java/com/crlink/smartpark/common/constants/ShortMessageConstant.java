package com.crlink.smartpark.common.constants;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class ShortMessageConstant {

    public static final int ALL = 3;
    public static final int TEL_VALIDATE_ONLY = 2;
    public static final int VALIDATE_CODE_ONLY = 1;

    public enum TemplateEnum {

        DEFAULT_TEMPLATE(0, "阿里云短信测试", "SMS_154950909", "sms:code:default:", 6, 15, TimeUnit.MINUTES, "默认"),
        OA_BIND(1, "阿里云短信测试", "SMS_154950909", "sms:code:default:", 6, 15, TimeUnit.MINUTES, "公众号绑定");


        @Getter
        private final int type;
        @Getter
        private final String signName;
        @Getter
        private final String templateCode;
        @Getter
        private final String redisPrefix;
        @Getter
        private final int length;
        @Getter
        private final long validMinute;
        @Getter
        private final TimeUnit timeUnit;

        private final String desc;

        TemplateEnum(int type, String signName, String templateCode, String redisPrefix, int length, long validMinute, TimeUnit timeUnit, String desc) {
            this.type = type;
            this.signName = signName;
            this.templateCode = templateCode;
            this.redisPrefix = redisPrefix;
            this.length = length;
            this.validMinute = validMinute;
            this.timeUnit = timeUnit;
            this.desc = desc;
        }

        public String buildRedisCode(String key) {
            return redisPrefix + key;
        }

        public static TemplateEnum getTemplateByType(int type) {
            for (TemplateEnum value : values()) {
                if (value.getType() == type) {
                    return value;
                }
            }
            return DEFAULT_TEMPLATE;
        }
    }
}
