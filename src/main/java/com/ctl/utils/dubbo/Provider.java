package com.ctl.utils.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 运行时需要启动zooKeeper
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( new String[] {"/dubbo/provider.xml"});
        context.start();
        // press any key to exit
        System.in.read();
    }
}