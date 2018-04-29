package com.ctl.utils.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:36:01
 * @package_name com.ctl.proxy.jdk
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class BookFacadeProxy implements InvocationHandler {
	private Object target;
	private static Logger logger = LoggerFactory.getLogger(BookFacadeProxy.class);
	/**
	 * 绑定委托对象并返回一个代理类
	 * 
	 * @param target
	 * @return
	 */
	public Object bind(Object target) {
		this.target = target;
		// 取得代理对象
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(), this); // 要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
	}

	/**
	 * 调用方法
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		logger.info("事物开始");
		// 执行方法
		result = method.invoke(target, args);
		logger.info("事物结束");
		return result;
	}
}