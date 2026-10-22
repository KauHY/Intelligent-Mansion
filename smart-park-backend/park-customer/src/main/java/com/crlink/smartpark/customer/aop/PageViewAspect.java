package com.crlink.smartpark.customer.aop;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.IpUtils;
import com.crlink.smartpark.core.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    public static final String INFORMATION = "information";
    public static final String NOTICE = "notice";

    @Autowired
    private RedisUtil redisUtil;


    @Pointcut("@annotation(PageView)")
    public void PageViewAspect() {

    }

    /**
     * 切入处理
     *
     * @param joinPoint
     * @return Object
     */
    @Around("PageViewAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        String queryId = (String) object[0];
        log.info("articleId:{}", queryId);
        Object obj = null;
        try {
            String ipAddr = IpUtils.getIpAddr();
            if (log.isDebugEnabled()) {
                log.debug("ipAddr:{}", ipAddr);
            }
            // 浏览量存入redis中
            //Long num = redisUtil.add(infoId, ipAddr);
            //if (num == 0) {
            //    log.info("该ip:{},访问的浏览量已经新增过了", ipAddr);
            //}
            String name = joinPoint.getSignature().getName();
            if (StrUtil.startWith(name, INFORMATION)) {
                setViews(queryId, Constants.Redis.INFORMATION_PAGE_VIEW_PREFIX.getBuildCode(queryId));
            } else if (StrUtil.startWith(name, NOTICE)) {
                setViews(queryId, Constants.Redis.NOTICE_PAGE_VIEW_PREFIX.getBuildCode(queryId));
            }
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("公告资讯浏览量记录出错，id：{}", queryId, e);
        }
        return obj;
    }

    private void setViews(String id, String formatStr) {
        Integer views = (Integer) redisUtil.getObject(String.format(formatStr, id));
        if (views == null) {
            redisUtil.setObject(String.format(formatStr, id), 0, 24L, TimeUnit.HOURS);
        }
        redisUtil.incr(String.format(formatStr, id), 1);
    }
}
