package com.ctl.utils.proxy.advice.before;

import com.ctl.utils.proxy.advice.introduction.Monitorable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * @author guolin
 * @date 2015-9-17下午4:35:19
 * @package_name com.ctl.proxy.before.advice
 * @project_name ctlUtils
 * @version version.1.0
 */
public class BeforeAdviceTest {
    static Logger logger = LoggerFactory.getLogger(BeforeAdviceTest.class);
	public static void main(String[] args) {
		Waiter waiter = new NativeWaiter();
		BeforeAdvice before = new GreetingBeforeWaiter();
		//Spring 提供代理工厂
		ProxyFactory pf = new ProxyFactory();
		//指定接口进行代理 可以注释掉
		pf.setInterfaces(waiter.getClass().getInterfaces());
		//设置代理目标
		pf.setTarget(waiter);
		//为代理目标添加增强
		pf.addAdvice(before);
		//设置启用优化
		pf.setOptimize(true);
		//waiter.greetTo("客户 greet");
		//waiter.serveTo("客户serve");
		waiter = (Waiter) pf.getProxy();
		waiter.greetTo("客户 greet");
		waiter.serveTo("客户serve");
		
		logger.info("--------------------------------------------------------------------------------");
		ApplicationContext application=new ClassPathXmlApplicationContext("classpath:/com/ctl/proxy/advice/before/applicationContext.xml");
		waiter=(Waiter) application.getBean("waiter");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterNotProxyInterfaces");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterAfterReturn");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterBeforeAndAfterReturn");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterBeforeAndAfterReturn1");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterAround");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		waiter=(Waiter) application.getBean("waiterService");
		waiter.greetTo("你好");
		logger.info("--------------------------------------------------------------------------------");
		Monitorable monitor=(Monitorable) application.getBean("waiterService");
		monitor.setMonitorActive(true);
		waiter.greetTo("你好");
	}
}
