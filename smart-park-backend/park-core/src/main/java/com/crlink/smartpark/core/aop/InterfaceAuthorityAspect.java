package com.crlink.smartpark.core.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.HeaderSource;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.HEADER_SOURCE;

@Component
@Aspect
@Slf4j
public class InterfaceAuthorityAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut1() {

    }

    //@Pointcut("@annotation(com.crlink.smartpark.core.annotation.InterfacePermission)")
    //public void pointcut2() {
    //    //排除自身的接口
    //}

    //@Pointcut("within(com.crlink.smartpark.*.feign..*)")
    //public void pointcut2() {
    //    //排除自身的接口
    //}

    //@Before("pointcut1()&&!pointcut2()")
    @Before("pointcut1()")
    public void doBefore(JoinPoint joinPoint) {
        Set<String> authSources = getAuthSource(joinPoint);
        String source = getSource();
        if (Objects.nonNull(source) && !CollectionUtil.contains(authSources, source)) {
            throw new ResolinkRuntimeException("10023");
        }
    }

    private Set<String> getAuthSource(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> aClass = joinPoint.getTarget().getClass();
        InterfacePermission annotation = AnnotationUtil.getAnnotation(method, InterfacePermission.class);
        HeaderSource[] source = null;
        if (annotation == null) {
            annotation = AnnotationUtil.getAnnotation(aClass, InterfacePermission.class);
        }
        if (annotation != null) {
            source = annotation.source();
        } else {
            source = HeaderSource.values();
        }
        Set<String> sources = Arrays.stream(source).map(t -> t.source).collect(Collectors.toSet());
        return sources;
    }

    private String getSource() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String header = request.getHeader(Constants.HEADER_USER_INFO);
        JSONObject jsonObject = JSONUtil.parseObj(header);
        String source = jsonObject.getStr(HEADER_SOURCE);
        return source;
    }
}
