package com.ctl.web.activemq;

import com.ctl.web.activemq.service.ConsumerService;
import com.ctl.web.activemq.service.ProducerService;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import java.util.Arrays;

public class ActivemqTest {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/spring-activemq.xml");
        System.out.println(Arrays.deepToString(ac.getBeanDefinitionNames()));
        ConsumerService consumerService = (ConsumerService) ac.getBean("consumerService");
        ProducerService producerService = (ProducerService) ac.getBean("producerService");
        Destination destination = (Destination) ac.getBean("demoQueueDestination");
        producerService.sendMessage(destination, "123");
        for (int i = 0; i < 10; i++) {
            //如果配置了监听器，则consumerService 无法获取到数据，监听器与 consumerService.receive(destination);只能选择一个
            consumerService.receive(destination);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
    }
}
