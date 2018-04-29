package com.ctl.utils.rabbitmq; /**@author 		guolin
 *  @date    		2015-11-7上午1:07:26
 *  @package_name 
 *  @project_name   RabbitMQ
 *  @version 		version.1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Recv
{
	//队列名称
	private final static String QUEUE_NAME = "hello2";

	public static void main(String[] argv) throws Exception,
			InterruptedException
	{
		//打开连接和创建频道，与发送端一样
		ConnectionFactory factory = new ConnectionFactory();
		//factory.setHost("localhost");
		factory.setHost(RabbitmqConfig.host);
		factory.setUsername(RabbitmqConfig.username);
		factory.setPassword(RabbitmqConfig.passward);
		factory.setVirtualHost(RabbitmqConfig.virtualHost);
		factory.setPort(RabbitmqConfig.port);
		factory.setRequestedHeartbeat(0);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		//创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//指定消费队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
		while (true)
		{
			//nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}

	}
}
