package com.ctl.utils.proxy.advice.around;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**@author 		guolin
 *  @date    		2015-9-17下午7:10:44
 *  @package_name com.ctl.proxy.advice.around
 *  @project_name   ctlUtils
 *  @version 		version.1.0
 */
public class GreetingAroundWaiter implements MethodInterceptor{
    static Logger logger = LoggerFactory.getLogger(GreetingAroundWaiter.class);
    public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("执行方法前(around)......");
		Object[] args=invocation.getArguments();//目标方法入参
		for(Object obj:args){
			logger.info(obj.toString());
		}
		Object obj=invocation.proceed();//通过反射机制调用目标方法
		logger.info("执行方法后(around)......");
		return obj;
	}
}
