package com.ctl.utils.proxy.advice.point.regex;

import com.ctl.utils.proxy.advice.before.NativeSellter;
import com.ctl.utils.proxy.advice.before.NativeWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**@author 		guolin
 *  @date    		2015-9-18下午1:38:07
 *  @package_name com.ctl.proxy.advice.point
 *  @project_name   ctlUtils
 *  @version 		version.1.0
 */
public class GreetingTest {
	private static Logger logger = LoggerFactory.getLogger(GreetingTest.class);
	public static void main(String[] args) {
		logger.info("--------------------------------------------------------------------------------");
		ApplicationContext application=new ClassPathXmlApplicationContext("classpath:/com/ctl/proxy/advice/point/regex/applicationContext.xml");
		NativeWaiter waiter= (NativeWaiter) application.getBean("waiter");
		NativeSellter seller= (NativeSellter) application.getBean("seller");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		seller.greetTo("你好");
	}
}
