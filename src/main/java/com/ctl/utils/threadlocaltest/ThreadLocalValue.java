package com.ctl.utils.threadlocaltest;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2016-1-25下午2:45:52
 * @package_name com.test
 * @project_name wastest
 * @version version.1.0
 * @description
 */
class ClientThread implements Runnable {
	private ThreadLocalValue local;

	public ClientThread() {
	}

	public ClientThread(ThreadLocalValue threadLocal) {
		this.local = threadLocal;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println("thread[" + Thread.currentThread().getName()
					+ "] sn=" + local.getNextSeq());
		}
	}
}

public class ThreadLocalValue {
	private static ThreadLocal<Integer> threadLocal1 = new ThreadLocal<Integer>() {
		public Integer initialValue() {
			return 0;
		}
	};

	public Integer getNextSeq() {
		threadLocal1.set(threadLocal1.get() + 1);
		return threadLocal1.get();
	}
}
