package com.ctl.utils.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicProducer {
    static Logger logger = LoggerFactory.getLogger(TopicProducer.class);

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
        //消息的主题
        Topic topic = null;
        //消息生产者
        MessageProducer messageProducer = null;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建名为TopicTest的主题
            topic = session.createTopic(ActiveMQConfig.TOPIC_NAME);
            //创建主题生产者
            messageProducer = session.createProducer(topic);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//NON_PERSISTENT 不将数据持久化 PERSISTENT 持久化
            //发送主题
            TextMessage message = null;
            for (int i = 0; i < 10; i++) {
                //创建要发送的文本信息
                message = session.createTextMessage("Topic主题测试" + (i + 1));
                //通过主题生产者发出消息
                messageProducer.send(message);
                System.out.println("发送成功：" + message.getText());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}