package com.ctl.web.activemq.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Service
public class ConsumerService {
    @Resource(name="jmsTemplate")
    private JmsTemplate jmsTemplate;

    public TextMessage receive(Destination destination){
        TextMessage textMessage = (TextMessage) jmsTemplate.receive(destination);
        try{
            if(textMessage!=null) {
                System.out.println("从队列" + destination.toString() + "收到了消息：\t" + textMessage.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return textMessage;
    }
}
