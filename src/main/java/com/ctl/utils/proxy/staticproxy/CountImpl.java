package com.ctl.utils.proxy.staticproxy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:26:19
 * @package_name com.ctl.proxy.staticproxy
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class CountImpl implements Count {
	private static Logger logger= LoggerFactory.getLogger(CountImpl.class);
	public void queryCount() {
		logger.info("查看账户方法...");
	}

	public void updateCount() {
		logger.info("修改账户方法...");
	}

}