package com.ctl.utils.proxy.advice.before;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author guolin
 * @date 2015-9-17下午4:32:59
 * @package_name com.ctl.proxy.before.advice
 * @project_name ctlUtils
 * @version version.1.0
 */
public class GreetingBeforeWaiter implements MethodBeforeAdvice {
    static Logger logger = LoggerFactory.getLogger(GreetingBeforeWaiter.class);

    public void before(Method arg0, Object[] arg1, Object arg2)
			throws Throwable {
		logger.info("做事之前......");
		logger.info(arg0.toString());
		for(Object obj:arg1){
			logger.info(obj.toString());
		}
		logger.info(arg2.toString());
	}
}
