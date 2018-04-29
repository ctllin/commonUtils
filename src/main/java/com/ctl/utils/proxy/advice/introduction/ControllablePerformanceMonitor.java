package com.ctl.utils.proxy.advice.introduction;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/**
 * @author guolin
 * @date 2015-9-17下午7:35:52
 * @package_name com.ctl.proxy.advice.introduction
 * @project_name ctlUtils
 * @version version.1.0
 */
public class ControllablePerformanceMonitor extends
		DelegatingIntroductionInterceptor implements Monitorable {
	private static final long serialVersionUID = 6909671823680514666L;
	private ThreadLocal<Boolean> MonitorStatusMap = new ThreadLocal<Boolean>();
	static Logger logger = LoggerFactory.getLogger(ControllablePerformanceMonitor.class);

	public Object invoke(MethodInvocation mi) throws Throwable {
		Object obj = null;
		if (MonitorStatusMap.get() != null && MonitorStatusMap.get()) {
		logger.info(mi.getClass().getName() + "."
					+ mi.getMethod().getName());
			obj = super.invoke(mi);
			logger.info("end......");
		} else {
			obj = super.invoke(mi);
		}
		return obj;
	}

	public void setMonitorActive(boolean active) {
		MonitorStatusMap.set(active);
	}
}
