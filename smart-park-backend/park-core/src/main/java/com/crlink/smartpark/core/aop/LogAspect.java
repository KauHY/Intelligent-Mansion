package com.crlink.smartpark.core.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crlink.smartpark.common.dto.OperateLogDTO;
import com.crlink.smartpark.common.feign.OperateLogProvider;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private OperateLogProvider operateLogProvider;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut1() {
    }

    @Pointcut("@annotation(com.crlink.smartpark.core.annotation.Exclude)")
    public void pointcut2() {
        //排除自身的接口
    }

    @Pointcut("pointcut1()&&!pointcut2()")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object recordOperationLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String userId = SystemUtil.getInstance().getCurrentUserId();
        log.info("请求开始：{},url:{},user:{}", Optional.ofNullable(getApiOperation(joinPoint)).map(ApiOperation::value).orElse(""), request.getRequestURL(), userId);
        log.info("请求参数req:{}", parseParamStr(request, joinPoint));
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            int costTime = (int) (System.currentTimeMillis() - start);
            CompletableFuture.runAsync(() -> {
                doRecord(userId, request, joinPoint, costTime);
            });
            log.info("请求结束resp:{}", parseResult(result));
        }
    }

    private ApiOperation getApiOperation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        return apiOperation;
    }

    private String parseParamStr(HttpServletRequest request, JoinPoint joinPoint) {
        try {
            String result = "GET".equals(request.getMethod()) ? (new ObjectMapper()).writeValueAsString(request.getParameterMap()) : JSONUtil.toJsonStr(joinPoint.getArgs());
            return (StrUtil.isNotBlank(result) && result.length() > 100) ? result.substring(0, 100) : result;
        } catch (JsonProcessingException e) {
            log.error("JSON解析异常", e);
            return "";
        }
    }

    private String parseResult(Object result) {
        if (Objects.isNull(result)) {
            return StrUtil.EMPTY;
        }
        try {
            String respStr = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
            return (StrUtil.isNotBlank(respStr) && respStr.length() > 100) ? respStr.substring(0, 100) : respStr;
        } catch (Exception e) {
            log.error("JSON解析异常", e);
            return "";
        }
    }

    private void doRecord(String userId, HttpServletRequest request, JoinPoint joinPoint, int timeConsuming) {
        String ip = ServletUtil.getClientIP(request, null);
        ApiOperation apiOperation = getApiOperation(joinPoint);
        OperateLogDTO logDTO = new OperateLogDTO();
        logDTO.setUserId(userId);
        logDTO.setRequestUri(request.getRequestURI());
        logDTO.setIp(ip);
        if (StrUtil.hasBlank(logDTO.getRequestUri(), ip)) {
            return;
        }
        if (Objects.nonNull(apiOperation)) {
            logDTO.setOperateModule(apiOperation.tags()[0]);
            logDTO.setOperateName(apiOperation.value());
        }
        logDTO.setTimeConsuming(timeConsuming);
        operateLogProvider.saveOperateLog(logDTO);
    }

}
