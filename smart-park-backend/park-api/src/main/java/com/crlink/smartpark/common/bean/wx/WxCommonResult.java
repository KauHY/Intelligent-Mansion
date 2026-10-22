package com.crlink.smartpark.common.bean.wx;

import lombok.Data;

@Data
public class WxCommonResult {

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;
}
