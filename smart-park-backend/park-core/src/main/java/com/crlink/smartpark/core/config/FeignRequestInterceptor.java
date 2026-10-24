package com.crlink.smartpark.core.config;

import com.crlink.smartpark.common.constants.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 为跨服务调用设置用户信息
 * @Created by ZHOUXINJIAN3
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            String headerUserInfo = request.getHeader(Constants.HEADER_USER_INFO);
            template.header(Constants.HEADER_USER_INFO, headerUserInfo);
        }
    }
}


