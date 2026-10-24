package com.crlink.smartpark.system.aop;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.crlink.smartpark.common.constants.Constants.USER_PLATFORM_SOURCE;

@Component
@Aspect
@Slf4j
public class PlatformTokenAspect {
    @Pointcut("pointcut1()&&!pointcut2()")
    private void pointcut() {
    }

    @Pointcut("within(com.crlink.smartpark.system.controller.platform.*)")
    public void pointcut1() {

    }

    @Pointcut("execution(* com.crlink.smartpark.system.controller.platform.PlatformAuthController.create(..))")
    public void pointcut2() {
        //排除登录接口
    }

    @Before("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        String source = SystemUtil.getInstance().getSource();
        if (Objects.equals(USER_PLATFORM_SOURCE, source)) {
            return;
        }
        log.info("【请求出错，source:{}】", source);
        throw new ResolinkRuntimeException("10001");

    }
}
