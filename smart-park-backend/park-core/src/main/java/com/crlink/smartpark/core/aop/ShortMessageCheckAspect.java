package com.crlink.smartpark.core.aop;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.annotations.ShortMessageCheck;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.ShortMessageConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.annotation.PermissionAnnotation;
import com.crlink.smartpark.core.utils.RedisUtil;
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
public class ShortMessageCheckAspect {

    public static final String FIELD_VALIDATE_CODE = "validateCode";
    public static final String FIELD_VALIDATE_CODE_KEY = "validateCodeKey";
    public static final String FIELD_MOBILE = "mobile";
    public static final String FIELD_TEL_VALIDATE_CODE = "telValidateCode";

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.crlink.smartpark.common.annotations.ShortMessageCheck)")
    public void pointAuthCut() {
    }

    @Before("pointAuthCut()&&@annotation(shortMessageCheck)")
    public void doBefore(JoinPoint joinPoint, ShortMessageCheck shortMessageCheck) throws Exception {
        Object[] args = joinPoint.getArgs();
        int type = shortMessageCheck.type();
        if ((type & ShortMessageConstant.VALIDATE_CODE_ONLY) == ShortMessageConstant.VALIDATE_CODE_ONLY) {
            String validateCode = (String) ReflectUtil.getFieldValue(args[0], FIELD_VALIDATE_CODE);
            String validateCodeKey = (String) ReflectUtil.getFieldValue(args[0], FIELD_VALIDATE_CODE_KEY);
            AssertUtil.requireNotNull(validateCode, "40029");
            AssertUtil.requireNotNull(validateCodeKey, "40030");
            checkValidateCode(validateCode, validateCodeKey);
        }

        if ((type & ShortMessageConstant.TEL_VALIDATE_ONLY) == ShortMessageConstant.TEL_VALIDATE_ONLY) {
            String mobile = (String) ReflectUtil.getFieldValue(args[0], FIELD_MOBILE);
            String telValidateCode = (String) ReflectUtil.getFieldValue(args[0], FIELD_TEL_VALIDATE_CODE);
            AssertUtil.requireNotNull(mobile, "40032");
            AssertUtil.requireNotNull(telValidateCode, "40031");
            checkTelValidate(mobile, telValidateCode, shortMessageCheck.telRedisPrefix().getRedisPrefix());
        }
    }

    private void checkValidateCode(String validateCode, String validateCodeKey){
        String oldValidateCode = (String)redisUtil
                .getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(validateCodeKey));
        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(validateCodeKey));
        AssertUtil.requireTrue(StrUtil.equalsIgnoreCase(validateCode, oldValidateCode ), "40004");
    }

    private void checkTelValidate(String mobile, String telValidateCode, String prefix){
        String smsCode = (String)redisUtil
                .getObject(prefix + mobile);
        AssertUtil.requireTrue(StrUtil.equalsIgnoreCase(telValidateCode, smsCode), "40012");
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


