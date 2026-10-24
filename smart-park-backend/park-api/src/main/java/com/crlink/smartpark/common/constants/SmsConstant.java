package com.crlink.smartpark.common.constants;

public interface SmsConstant {

    /**
     * redis key
     */
    String REDIS_KEY = "12345678aqwer!";

    /**
     * 短信签名
     */
    String SIGN_NAME = "淮安智慧谷";

    /**
     * 企业端用户注册验证码下发
     */
    String COMPANY_REGISTER_CODE = "SMS_243566533";

    /**
     * 企业端忘记密码短信验证码模板
     */
    String COMPANY_TEMPLATE_CODE = "SMS_243392928";

    /**
     * 运营端忘记密码短信验证码模板
     */
    String PARK_TEMPLATE_CODE = "SMS_243392925";

    /**
     * 用户密码下发短信模板
     */
    String PARK_PWD_CODE = "SMS_243437959";

    /**
     * 访客预约被访人下发模板
     */
    String PARK_VISITOR_CODE = "SMS_245090841";

    /**
     * 访客预约访客下发模板
     */
    String PARK_VISITOR_TEL_CODE = "SMS_245150639";

    /**
     * 访客邀约下发模板
     */
    String PARK_VISITOR_INVITE_CODE = "SMS_245145675";


}
