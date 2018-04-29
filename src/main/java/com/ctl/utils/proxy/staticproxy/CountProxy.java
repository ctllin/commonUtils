package com.ctl.utils.proxy.staticproxy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:23:33
 * @package_name com.ctl.proxy.staticproxy
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class CountProxy implements Count {
	private static Logger logger= LoggerFactory.getLogger(CountProxy.class);

	private CountImpl countImpl;

	/**
	 * 覆盖默认构造器
	 * 
	 * @param countImpl
	 */
	public CountProxy(CountImpl countImpl) {
		this.countImpl = countImpl;
	}

	public void queryCount() {
		logger.info("事务处理之前");
		// 调用委托类的方法;
		countImpl.queryCount();
		logger.info("事务处理之后");
	}

	public void updateCount() {
		logger.info("事务处理之前");
		// 调用委托类的方法;
		countImpl.updateCount();
		logger.info("事务处理之后");
	}
}