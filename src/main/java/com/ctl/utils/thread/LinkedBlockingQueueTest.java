package com.ctl.utils.thread;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueueTest {
	public static ThreadLocal<LinkedBlockingQueue<User>> queueHoder = new ThreadLocal<LinkedBlockingQueue<User>>();
	public static LinkedBlockingQueue<User> getQueue() {
		LinkedBlockingQueue<User> queue=queueHoder.get();
		if(queue==null)
		{
			queue=new LinkedBlockingQueue<User>(3);
			queueHoder.set(queue);
			return queue;
		}
		return queue;
	}

	static void start() {
		Tom tom = new Tom();
		Jerry jerry = new Jerry();
		jerry.start();
		tom.start();
	}

	public static void main(String[] args) {
		LinkedBlockingQueueTest.start();
	}
}

class Tom extends Thread {
	LinkedBlockingQueue<User> queue=LinkedBlockingQueueTest.getQueue();

	public void run() {
		boolean result = false;
		for (int i = 0; i < 50; i++) {
			try {
				result = queue.offer(new User(1, "ctl" + i,"lin","518","F"), 1,
						TimeUnit.SECONDS);
				if (result == false) {
					System.out.println("满了");
					--i;
				} else {
					System.out.println("Tom 添加信息成功：" + new User(1, "ctl" + i,"lin","518","F"));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Jerry extends Thread {
	LinkedBlockingQueue<User> queue=LinkedBlockingQueueTest.getQueue();
	public void run() {
		for (int i = 0; i < 50; i++) {
			try {
				User user =new User();
				user=queue.poll(2, TimeUnit.SECONDS);
				System.out.println("user"+user);
				if (user == null) {
					--i;
					continue;
				} else {
					System.out.println("Jerry 获取信息成功：" + user);
				}
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}