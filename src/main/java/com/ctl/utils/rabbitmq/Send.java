package com.ctl.utils.rabbitmq; /**@author 		guolin
 *  @date    		2015-11-7上午1:05:30
 *  @package_name 
 *  @project_name   RabbitMQ
 *  @version 		version.1.0
 */

import java.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send
{
	//队列名称
	private final static String QUEUE_NAME = "hello2";

	public static void main(String[] argv) throws Exception
	{
		/**
		 * 创建连接连接到MabbitMQ
		 */
		ConnectionFactory factory = new ConnectionFactory();
		//设置MabbitMQ所在主机ip或者主机名
		//factory.setHost("localhost");
		factory.setHost(RabbitmqConfig.host);
		factory.setUsername(RabbitmqConfig.username);
		factory.setPassword(RabbitmqConfig.passward);
		factory.setVirtualHost(RabbitmqConfig.virtualHost);
		factory.setPort(RabbitmqConfig.port);
		factory.setRequestedHeartbeat(0);
		//创建一个连接
		Connection connection = factory.newConnection();
		//创建一个频道
		Channel channel = connection.createChannel();
		//指定一个队列  rabbitmq中已经定义过的非持久化队列再次定义为持久化队列报错问题
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//发送的消息
		StringBuilder message = new StringBuilder("hello world!");
		//往队列中发出一条消息
		int i=0;
		while(i<10000){
			message.delete(0, message.length());
			message=message.append("\t").append(UUID.randomUUID().toString().replaceAll("-",""));
			channel.basicPublish("", QUEUE_NAME, null, message.toString().getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//关闭频道和连接
		channel.close();
		connection.close();
	 }
}
