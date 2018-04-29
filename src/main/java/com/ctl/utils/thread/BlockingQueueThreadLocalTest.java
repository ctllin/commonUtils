package com.ctl.utils.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueThreadLocalTest {
	public static ThreadLocal<BlockingQueue<User>> queueHoder = new ThreadLocal<BlockingQueue<User>>();

	public static BlockingQueue<User> getQueue() {
		BlockingQueue<User> queue = queueHoder.get();
		if (queue == null) {
			queue = new LinkedBlockingQueue<User>(3);
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
		BlockingQueueThreadLocalTest.start();
	}
}
