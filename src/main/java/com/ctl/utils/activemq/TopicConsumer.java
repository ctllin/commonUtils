package com.ctl.utils.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * topic 消费者需要早于生产者启动，否则消费者无法消费生产者的消息
 */
public class TopicConsumer {
    static Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    public static void main(String[] args) {
        //连接信息设置
        String username = "system";
        String password = "manager";
        String brokerURL = ActiveMQConfig.TOPIC_BROKEN_URL;
        //连接工厂
        ConnectionFactory connectionFactory = null;
        //连接
        Connection connection = null;
        //会话 接受或者发送消息的线程
        Session session = null;
        //主题的目的地
        Topic topic = null;
        //主题消费者
        MessageConsumer messageConsumer = null;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建一个连接TopicTest的主题
            topic = session.createTopic(ActiveMQConfig.TOPIC_NAME);
            //创建主题消费者
            messageConsumer = session.createConsumer(topic);

            messageConsumer.setMessageListener(new MyMessageListener());
        } catch (JMSException e) {
            logger.error("topic",e);
        }
    }

}

class MyMessageListener implements MessageListener {
    static Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            textMessage.acknowledge();
            System.out.println("接收订阅主题：" + textMessage.getText());
        } catch (JMSException e) {
            logger.error("接收订阅主题失败",e);
        }
    }

}
