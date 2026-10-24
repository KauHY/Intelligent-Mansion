package com.crlink.smartpark.common.bean.wx;

import lombok.Data;

@Data
public class WxOaSessionDTO extends WxCommonResult {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 调用凭证
     */
    private String access_token;

    /**
     * 调用凭证超时时间
     */
    private String expires_in;

    private String refresh_token;

    private String scope;

}
