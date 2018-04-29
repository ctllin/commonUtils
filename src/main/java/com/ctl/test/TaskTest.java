package com.ctl.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.management.ManagementFactory;

/**
 * <p>Title: TaskTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.ctl.com</p>
 *
 * @author ctl
 * @version 1.0
 * @date 2018-04-26 10:47
 */
public class TaskTest {
    public static Logger logger = LoggerFactory.getLogger(TaskTest.class);
    public static void main(String[] args) {
        ApplicationContext context=  new ClassPathXmlApplicationContext("spring/spring_mvc.xml");//名字只可以是applicationContext.xml
        String pidName= ManagementFactory.getRuntimeMXBean().getName();
        logger.info("线程MyBatisPaginatorTest pidName:"+pidName);
    }
}
