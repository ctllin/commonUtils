package com.ctl.utils.proxy.spring;

import java.lang.reflect.Method;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:41:11
 * @package_name com.ctl.proxy.spring
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class BookFacadeCglib implements MethodInterceptor {
	private static Logger logger = LoggerFactory.getLogger(BookFacadeCglib.class);
	private Object target;

	/**
	 * 创建代理对象
	 * @param target
	 * @return
	 */
	public Object getInstance(Object target) {
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.target.getClass());
		// 回调方法
		enhancer.setCallback(this);
		// 创建代理对象
		return enhancer.create();
	}

	
	// 回调方法
	public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
		logger.info("事物开始");
		proxy.invokeSuper(obj, args);
		logger.info("事物结束");
		return null;
	}

}
