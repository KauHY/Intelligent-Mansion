package com.crlink.smartpark.core.aop;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.annotation.PermissionAnnotation;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.feign.PermissionProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Set;

@Component
@Aspect
@Slf4j
public class AnnotationAspect {

    @Resource
    private PermissionProvider permissionProvider;

    @Pointcut("@annotation(com.crlink.smartpark.core.annotation.PermissionAnnotation)")
    public void pointAuthCut() {
    }

    @Before("pointAuthCut()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        String value = getPermissionAnnotationValue(joinPoint);
        Set<String> urls = permissionProvider.getMenuUrlsByUser(SystemUtil.getInstance().getCurrentUserId());
        if (!urls.contains(value)) {
            throw new ResolinkRuntimeException("40008");
        }
    }

    /**
     * 获取注解信息
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private String getPermissionAnnotationValue(JoinPoint joinPoint) {
        // 获取切入点的目标类
        Class<?> aClass = joinPoint.getTarget().getClass();
        String targetName = joinPoint.getTarget().getClass().getName();
        Class<?> targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 获取切入方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取切入方法参数
        Object[] arguments = joinPoint.getArgs();
        // 获取目标类的所有方法
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            // 方法名相同、包含目标注解、方法参数个数相同（避免有重载）
            if (method.getName().equals(methodName) && method.isAnnotationPresent(PermissionAnnotation.class)
                    && method.getParameterTypes().length == arguments.length) {
                return method.getAnnotation(PermissionAnnotation.class).value();
            }
        }
        return "";
    }
}


