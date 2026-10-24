package com.crlink.smartpark.common.bean.wx;

import lombok.Data;

import java.util.Map;

@Data
public class WxTelDTO {

    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 区号
     */
    private String countryCode;

    /**
     * 水印
     */
    private Map<String,String> watermark;
}
