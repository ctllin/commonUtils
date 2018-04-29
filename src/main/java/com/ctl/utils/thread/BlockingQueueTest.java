package com.ctl.utils.thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*阻塞队列* */
public class BlockingQueueTest {
	Thread tom;// tom receive
	Thread jerry;// jerry send
	BlockingQueue<String> queue;

	class Tom extends Thread {
		public void run() {
			while (true) {
				try {
					String msg=queue.poll(1, TimeUnit.SECONDS);
					if(msg==null){continue;}
					System.out.println("Tom receive:   "+msg);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					System.err.println("InterruptedException"+e);
				}
			}
		}
	}

	class Jerry extends Thread {
		int i = 0;

		public void run() {
			while (true) {
				String hello = "HI!" + i;
				try {
					// 如果等待5秒插入失败返回false成功返回true
					boolean success = queue.offer(hello, 2, TimeUnit.SECONDS);
					// 如果插入成功 i++ 否则继续插入
					if (success) {
						System.out.println("添加信息成功\t"+hello);
						i++;
					} else {
						System.out.println("满了，重试");
						continue;
					}
				} catch (InterruptedException e) {
					System.err.println("InterruptedException" + e);
				}
			}
		}
	}

	public void start() {
		queue = new LinkedBlockingQueue<String>(3);// 超过3个就满了
		tom = new Tom();
		jerry = new Jerry();
		tom.start();
		jerry.start();
	}

	public static void main(String[] args) {
		new BlockingQueueTest().start();
	}

}
