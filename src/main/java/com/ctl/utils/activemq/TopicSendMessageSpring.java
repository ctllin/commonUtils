package com.ctl.utils.activemq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class TopicSendMessageSpring {
    static Logger logger = LoggerFactory.getLogger(TopicSendMessageSpring.class);

    private ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:activemq/spring-activemq.xml");
    private JmsTemplate jmsTemplate = (JmsTemplate) ac.getBean("jmsTemplate");
    public void send(){


        jmsTemplate.send(new MessageCreator() {


            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                    TextMessage msg = session.createTextMessage();
                    msg.setText("\t data:"+System.currentTimeMillis());
                    System.out.println("发送数据++++++++++++发送数据");
                    return  msg;
            }
        });
    }


    public static void main(String[] args) {
        TopicSendMessageSpring topicSendMessageSpring = new TopicSendMessageSpring();
        for (int i = 0; i <10 ; i++) {
            topicSendMessageSpring.send();
        }
    }
}