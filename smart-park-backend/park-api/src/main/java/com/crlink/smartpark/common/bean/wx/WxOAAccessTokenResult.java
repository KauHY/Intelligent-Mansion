package com.crlink.smartpark.common.bean.wx;

import lombok.Data;

@Data
public class WxOAAccessTokenResult {

    private String accessToken;
    //有效时间-秒
    private Long expiresIn;
}
