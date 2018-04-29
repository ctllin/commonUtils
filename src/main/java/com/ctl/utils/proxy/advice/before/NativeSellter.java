package com.ctl.utils.proxy.advice.before;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**@author 		guolin
 *  @date    		2015-9-18上午10:41:38
 *  @package_name com.ctl.proxy.advice.before
 *  @project_name   ctlUtils
 *  @version 		version.1.0
 */
public class NativeSellter {
    static Logger logger = LoggerFactory.getLogger(NativeSellter.class);
	public void greetTo(String name) {
		logger.info(name);
	}
}
