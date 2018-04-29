package com.ctl.utils.proxy.advice.point.auto;

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
		ApplicationContext application=new ClassPathXmlApplicationContext("classpath:/com/ctl/proxy/advice/point/auto/applicationContext.xml");
		NativeWaiter waiter= (NativeWaiter) application.getBean("waiterTarget");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter.greetTo("Jone");
		logger.info("--------------------------------------------------------------------------------");
		waiter.greetTo("Jone");
	}
}
