package com.ctl.utils.proxy.advice.point.general;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;

/**
 * @author guolin
 * @date 2015-9-17下午4:32:59
 * @package_name com.ctl.proxy.before.advice
 * @project_name ctlUtils
 * @version version.1.0
 */
public class GreetingAfterReturningAdvice implements AfterReturningAdvice{
	private static Logger logger = LoggerFactory.getLogger(GreetingAfterReturningAdvice.class);
	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {
		logger.info(arg0.toString());
		logger.info(arg1.toString());
		for(Object obj:arg2){
			logger.info(obj.toString());
		}
		logger.info("做事之后......");
	}
}
