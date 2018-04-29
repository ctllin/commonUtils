package com.ctl.utils.proxy.advice.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @date 2015-9-17下午4:30:06
 * @package_name com.ctl.proxy.before.advice
 * @project_name ctlUtils
 * @version version.1.0
 */
public class NativeWaiter implements Waiter {
    static Logger logger = LoggerFactory.getLogger(NativeWaiter.class);
	public void greetTo(String name) {
		logger.info(name);
	}

	public void serveTo(String name) {
		logger.info(name);
	}
}