package com.ctl.utils.proxy.advice.point.general;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author guolin
 * @date 2015-9-18上午11:01:42
 * @package_name com.ctl.proxy.advice.point
 * @project_name ctlUtils
 * @version version.1.0
 */
public class GreetingBeforeAdvice implements MethodBeforeAdvice {
	private static Logger logger = LoggerFactory.getLogger(GreetingBeforeAdvice.class);
	public void before(Method method, Object[] args, Object obj)
			throws Throwable {
		logger.info(obj.getClass().getName() + "." + method.getName());
		for (Object objt : args) {
			logger.info(objt.toString());
		}
	}
}
