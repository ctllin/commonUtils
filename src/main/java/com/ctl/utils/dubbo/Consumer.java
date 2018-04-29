package com.ctl.utils.dubbo;

import com.ctl.web.dubbo.service.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 运行时需要启动zooKeeper
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/dubbo/consumer.xml"});
        context.start();
        // obtain proxy object for remote invocation
        HelloService helloService = (HelloService) context.getBean("helloService");
        // execute remote invocation
        String hello = helloService.say("world");
        // show the result
        System.out.println(hello);
    }
}