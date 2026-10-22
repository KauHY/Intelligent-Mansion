package com.crlink.smartpark.common.constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 系统常量池
 * </p>
 * */
public class Constants {

    public static final LocalDateTime LocalDateTime_MAX = LocalDateTime.of(LocalDate.of(2200, 12, 31), LocalTime.of(23, 59, 59));
    /**
     * 国际化资源前缀
     */
    public static final String GLOBALIZATION_RESOURCE_PREFIX = "i18n.infos";

    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 员工人脸图片文件夹
     */
    public static final String EMPLOYEE_FACE_PIC_DIR = "employee";
    /**
     * 车辆通行文件夹
     */
    public static final String CAR_ACCESS_DIR = "car_access";

    /**
     * 人员通行
     */
    public static final String PERSON_ACCESS_DIR = "person_access";

    /**
     * 登录超时时间
     */
    public static final int LOGIN_TIMEOUT = 30;


    public static final int DICT_PAGE_SIZE = 100;
    /**
     * JSON返回头类型名称
     */
    public static final String JSON_TYPE_RESPONSE_HEADER_N = "Content-Type";
    /**
     * JSON返回头类型名值
     */
    public static final String JSON_TYPE_RESPONSE_HEADER_V = String.format("application/json;charset=%s", DEFAULT_CHARSET);
    /**
     * 成功返回编码
     */
    public static final int SUCCESS_CODE = 10000;
    /**
     * 成功返回提示
     */
    public static final String SUCCESS_MESSAGE = "成功!";
    /**
     * 失败返回编码
     */
    public static final int FAIL_CODE = 10001;
    /**
     * 逗号标识
     */
    public static final String COMMA_MARK = ",";
    public static final String DOT = ".";

    /**
     * 时间格式模板
     */
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String EXCEL_FILE_TIME_PATTERN = "yyyy_MM_dd_HH:mm:ss";
    public static final String EXCEL_DATE_PATTERN = "yyyy/M/d";
    public static final String EXCEL_TIME_PATTERN = "yyyy/M/d HH:mm:ss";

    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String SMALL_DATE_FORMAT = "yyyyMMdd";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_TIME_MINUTES_FORMAT = "HH:mm";
    public static final String DEFAULT_DATE_TIME_FORMAT_WITHOUT_YEAR = "MM-dd HH:mm";

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    public static final DateTimeFormatter EXPORT_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    private static final DateTimeFormatter DEFAULT_TIME_MINUTES_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_MINUTES_FORMAT);
    public static final DateTimeFormatter DEFAULT_MONTH_DAY_FORMAT = DateTimeFormatter.ofPattern(MONTH_DAY_FORMAT);

    public static final DateTimeFormatter DEFAULT_YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT);
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS);

    public static final DateTimeFormatter TIME_WITHOUT_SECONDS_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS);
    public static final DateTimeFormatter TIME_WITHOUT_YEAR_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_WITHOUT_YEAR);


    /**
     * 日期格式模板
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期格式模板-中文
     */
    public static final String DATE_PATTERN_CN = "yyyy年MM月dd日";

    /**
     * 日期格式模板-中文
     */
    public static final String DATE_PATTERN_MONTH_CN = "yyyy年MM月";
    /**
     * 中国时区
     */
    public static final String TIMEZONE = "GMT+8";

    /**
     * 18位数字
     */
    public static final String REGEXP_ONLY_18_NUMBER = "^\\d{18}$";

    /**
     * 统一社会信用代码校验
     * 统一社会信用代码由18位数字或者大写字母组成，但是字母不包括 I、O、Z、S、V
     * 一共由五部分组成：
     * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
     * 第二部分：机构类别代码1位 (数字或大写英文字母)
     * 第三部分：登记管理机关行政区划码6位 (数字)
     * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
     * 第五部分：校验码1位 (数字或大写英文字母)
     */
    public static final String REGEXP_COMPANY_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";
    /**
     * 纯数字
     */
    public static final String REGEXP_ONLY_NUMBER = "^[0-9]*$";

    public static final String ROLE_ORG_LIST_KEY = "role_org_list:%s";
    public static final String ROLE_ORG_LIST_KEY_PREFIX = "role_org_list";

    public static final String USER_MENU_LIST_KEY = "user_menu_list:%s";

    public static final String PLATFORM_ORG_ID = "0";
    public static final String PLATFORM_SYS_MENU_ORG_ID = "-1";
    // 内置超级管理员role 的ID
    public static final String PLATFORM_ADMIN_ROLE_ID = "1";
    public static final String HEAD_MENU_PARENT_ID = "0";
    public static final int TENANT_LEVEL = 1;
    //对用user表user_type，平台所属的用户和租户所属的用户
    public static final int PLATFORM_USER = 0;
    public static final int TENANT_USER = 1;
    //微信临时访客
    public static final int WECHAT_TEMP_USER = 2;
    public static final String WECHAT_USER_NAME = "wx";

    /**
     * 前端、后端密码中的salt;勿轻易更改
     */
    public static final String WEB_SALT = "_biubiubui~~~";
    public static final String SYSTEM_SALT = "++dadada_";

    /**
     * 密码正则:与前端一致
     */
//    public static final String REGEX_PASSWORD = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{8,20}$";


    public static final String QUERY_EMPTY_IN = "-1";

    /**
     * 物业类型公共 company_type
     */
    public static final int SPECIAL_COMPANY_TYPE = 0;


    /**
     * accessToken过期时间
     */
    public static final Long OVERTIME = 12 * 6000L; //12小时


    public static final Long OVERTIME_HALF = 6 * 60L;//6小时

    /**
     * refreshToken过期时间
     */
    public static final Long OVERTIME_REFRESH = 150L; //150MINUTES

    /**
     * refreshToken过期时间
     */
    public static final Long OVERTIME_REFRESH_SECOND = 9000L;

    /**
     * 时间
     */
    public static final Long TIME_SECOND = 0L;

    /**
     * 数字0
     */
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    /**
     * 微信accessToken过期时间(60天)
     */
    public static final Long WX_TOKEN_OVERTIME = 60 * 24 * 60L;

    public static final String HEADER_AUTH = "Authorization";
    //public static final String HEADER_SOURCE = "X-Request-Source";
    //public static final String HEADER_USER_ID = "rs-user-id";
    //public static final String HEADER_HEAD_ORG_ID = "head_org_id";
    //public static final String HEADER_USER_USERNAME = "rs-user-username";
    //public static final String HEADER_TOKEN_ID = "rs-token-id";
    //public static final String JWT_USER_ID = "id";
    //public static final String JWT_TOKEN_ID = "tokenId";
    //public static final String JWT_USER_NAME = "username";
    public static final String JWT_TS = "ts";


    public static final String HEADER_USER_INFO = "header-user-info";
    public static final String TOKEN_ID = "tokenId";
    public static final String USER_ID = "user-id";
    public static final String USER_NAME = "username";
    public static final String USER_MOBILE = "mobile";
    public static final String WX_USER_INFO = "wx_user_info";
    public static final String HEAD_ORG_ID = "head_org_id";
    public static final String HEAD_ORG_NAME = "head_org_name";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY_NAME = "companyName";

    //public static final String HEADER_PARK_MANAGEMENT_TOKEN = "X-Park-Management-Token"; //园区运营小程序
    //public static final String HEADER_PARK_VISITOR_TOKEN = "X-Park-Visitor-Token"; //园区访客小程序

    /**
     * 请求来源：
     * B：园区运营小程序   C：园区访客小程序  P：PC端来源
     */
    public static final String HEADER_SOURCE = "source"; //token来源
    public static final String USER_WX_SOURCE_B = "2"; //园区运营小程序
    public static final String USER_WX_SOURCE_C = "3";  //园区访客小程序
    public static final String USER_BUSINESS_SOURCE = "4";  //企业端运营平台
    public static final int TOKEN_PREFIX_INDEX = 7;  //

    public static final String USER_PLATFORM_SOURCE = "0";  // PC端来源,租户管理平台
    public static final String USER_PARK_MANAGEMENT_SOURCE = "1";  // PC端来源，运营管理平台


    /**
     * 根据Id查询real_name
     */
    public static final String USER_REAL_NAME = "real_name";

    /**
     * 微信登录用户无昵称real_name时使用nickname
     */
    public static final String NICK_NAME = "nickname";
    /**
     * 手机号正则
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[0-9])|(17[0-3,5-8])|(18[0-9])|(147)|(19[0-3,5-9]))\\d{8}$";

    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    /**
     * 密码正则
     */
    public static final String REGEX_PSW = "^(?![0-9]+$)(?![a-zA-Z~!@#$%^*]+$)[0-9A-Za-z~!@#$%^*]{8,20}$";

    /**
     * 车牌正则
     */
    public static final String REGEX_CAR_PLATE = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";

    /**
     * url正则
     */
    public static final String REGEX_URL = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";

    /**
     * 合同编号正则
     */
    public static final String REGEX_CONTRACT_NO = "^HT-ZL-[1-2]\\d{3}((0[1-9])|(1[0-2]))-\\d{6}$";


    /**
     * 移动端api路径
     */
    public static final String MOBILE_API_PATH_PATTERN = "/mobile/**";

    public static final String TRUE = "true";

    /**
     * HH:mm 正则
     */
    public static final String REMIND_TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
    public static final String REMIND_TIME_WITHOUT_SECOND_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";


    /**
     * 是否删除：0否1是
     */
    public enum Delete {
        NO(0, "否"), YES(1, "是");

        private Integer code;
        private String name;

        Delete(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }
    }


    /**
     * 状态 0已禁用,1正常
     */
    public enum Status {
        DISABLE(0, "已禁用"),
        NORMAL(1, "正常");

        public Integer code;
        public String name;

        Status(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }
    }

    /**
     * 状态 0-未生效,1-已生效，2-已失效
     */
    public enum CarStatus {
        NOT_ACTIVE(0, "未生效"),
        ACTIVATING(1, "已生效"),
        INVALIDATION(2, "已失效");

        private Integer code;
        private String name;

        CarStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }
    }

    /**
     * redis信息
     */
    public enum Redis {
        USER_INFO_PREFIX("user:info:", "用户信息前缀"),
        USER_BUSINESS_INFO_PREFIX("user:business:info:", "企业端用户信息前缀"),
        WX_USER_INFO_PREFIX("park-system:employee:wx:user:info:", "微信用户信息前缀"),
        USER_ACCESS_TOKEN_PREFIX("user:user_access_token:", "用户accessToken前缀"),
        WX_OA_ACCESS_TOKEN_PREFIX("wxOa:AccessToken", "公众号Token前缀"),
        WX_OA_PUNCH_REMINDER_TEMPLATE_PREFIX("wxOa:Template:PunchReminder", "公众号打卡提醒模板前缀"),
        PUNCH_REMINDER_IN_PREFIX("wxOa:punchReminderIn:", "上班打卡提醒前缀"),
        PUNCH_REMINDER_OUT_PREFIX("wxOa:punchReminderOut:", "下班打卡提醒前缀"),
        //USER_REFRESH_TOKEN_PREFIX("user:user_refresh_token:", "用户refreshToken前缀"),
        IMG_AUTH_CODE_PREFIX("img:auth:code:", "图形验证码"),
        BUSINESS_REGISTER_CODE_PREFIX("img:register:sms:code:", "企业端注册短信验证码"),
        RESET_PWD_CODE_PREFIX("reset-pwd:auto:sms:code:", "重置密码短信验证码"),
        DICTIONARY_TYPE_PREFIX("dictionary:", "字典前缀"),

        USER_PARK_ORG_IDS("park-system:organization:user:park_org_ids:", "用户拥有权限的园区ID集合"),
        TASK_LOCK_PREFIX("task:lock:", "定时任务分布式锁前缀"),
        HOLIDAY_PREFIX("holiday:", "法定节假日"),
        NOTICE_PAGE_VIEW_PREFIX("page_view:notice:", "公告浏览量前缀"),
        INFORMATION_PAGE_VIEW_PREFIX("page_view:information:", "资讯浏览量前缀"),
        CARD_NO_DEVICE_AUTHORITIES_PREFIX("device:door_access:card_no_device_ids_authorities:", "门禁设备卡号授权到设备ID集合缓存前缀"),
        CHANNEL_NO_DEVICE_AUTHORITIES_PREFIX("device:door_access:smart_lock:channel_no_device_ids_authorities:", "智能锁通道授权卡号ID集合缓存前缀"),
        IMPORT_EMPLOYEE_PREFIX("import_employee:cards_devicePersonIds:", "员工导入时批量获取的卡号-人员id前缀");

        private String code;
        private String name;

        Redis(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return this.code;
        }

        public String getBuildCode(String key) {
            return this.code + key;
        }

        public static String getOriginKey(String buildCode) {
            for (Redis value : values()) {
                if (buildCode.startsWith(value.code)) {
                    return buildCode.replace(value.code, "");
                }
            }
            return "";
        }
    }

    /**
     * 状态 0已禁用,1正常
     */
    public enum TimeTypeEnum {
        YEAR(1, "年"),
        MONTH(2, "月"),
        DAYS(3, "日");

        public Integer type;
        public String name;

        TimeTypeEnum(Integer type, String name) {
            this.type = type;
            this.name = name;
        }

        public Integer getType() {
            return type;
        }
    }

    /**
     * 打卡状态 0-未打卡,1-考勤异常，2-迟到，3-早退，4-正常打卡
     */
    public enum PunchStatus {
        ZERO(0, "未打卡"),
        ONE(1, "考勤异常"),
        LATE(2, "迟到"),
        EARLY(3, "早退"),
        NORMAL(4, "正常打卡");

        public Integer code;
        public String name;

        PunchStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }
    }
}
