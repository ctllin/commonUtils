package com.ctl.utils.proxy.advice.point.general;

import java.lang.reflect.Method;

import com.ctl.utils.proxy.advice.before.NativeWaiter;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * @author guolin
 * @date 2015-9-18上午10:43:44
 * @package_name com.ctl.proxy.advice.point
 * @project_name ctlUtils
 * @version version.1.0
 */
public class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor {
	private static final long serialVersionUID = -6850751716739541815L;

	public boolean matches(Method method, Class<?> clazz) {
		//切点方法匹配规则
		return "greetTo".equalsIgnoreCase(method.getName());
	}

	public ClassFilter getClassFilter() {
		//切点类匹配规则 为NativeWaiter类或其子类
		return new ClassFilter() {
			public boolean matches(Class<?> clazz) {
				return NativeWaiter.class.isAssignableFrom(clazz);
			}
		};
	}
}
