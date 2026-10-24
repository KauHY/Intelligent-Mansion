package com.crlink.smartpark.core.aop;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.feign.CommonProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;

@Component
@Aspect
@Slf4j
public class UserParkAspect {

	@Resource
	private CommonProvider commonProvider;

	@Pointcut("@annotation(com.crlink.smartpark.common.annotations.CurrentUserParkOrgIds)")
	public void pointCut() {
	}

	@Before("pointCut()")
	public void doBefore(JoinPoint joinPoint) throws Exception {
		Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
		if (CollectionUtils.isEmpty(parkOrgIds)) {
			throw new ResolinkRuntimeException("20016");
		}
	}
}
