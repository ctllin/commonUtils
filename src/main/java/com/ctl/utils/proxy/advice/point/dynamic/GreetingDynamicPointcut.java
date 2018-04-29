package com.ctl.utils.proxy.advice.point.dynamic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ctl.utils.proxy.advice.before.NativeWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
/**
 * @author guolin
 * @date 2015-9-18下午3:13:32
 * @package_name com.ctl.proxy.advice.point.dynamic
 * @project_name ctlUtils
 * @version version.1.0
 */
public class GreetingDynamicPointcut extends DynamicMethodMatcherPointcut {
	private static List<String> specialClientList = new ArrayList<String>();
	private static Logger logger = LoggerFactory.getLogger(GreetingDynamicPointcut.class);
	static {
		specialClientList.add("John");
		specialClientList.add("Tom");
	}
	
	public ClassFilter getClassFilter() {
		// 切点类匹配规则 为NativeWaiter类或其子类
		return new ClassFilter() {
			public boolean matches(Class<?> clazz) {
				logger.info("调用getClassFilter()对"+clazz.getName()+"做静态检查.");
				return NativeWaiter.class.isAssignableFrom(clazz);
			}
		};
	}

	public boolean matches(Method method, Class<?> clazz) {
		logger.info("调用matches(method,clazz)"+clazz.getName()+"."+method.getName()+"做静态检查.");
		// 切点方法匹配规则
		return "greetTo".equalsIgnoreCase(method.getName());
	}
	public boolean matches(Method method, Class<?> clazz, Object[] args) {
		logger.info("调用matches(method,clazz,args[])"+clazz.getName()+"."+method.getName()+"做动态检查.");
		// 切点方法匹配规则
		for(Object obj:args){
			logger.info(obj.toString());
		}
		return specialClientList.contains(args[0]);
	}
}