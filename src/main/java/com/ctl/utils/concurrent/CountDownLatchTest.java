package com.ctl.utils.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author guolin
 * @date 2016-11-17下午5:38:38
 * @package_name
 * @project_name SpringTest1
 * @version version.1.0
 */
class Worker implements Runnable {
	static Logger logger = LoggerFactory.getLogger(Worker.class);

	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;
	private final String threadName;

	Worker(CountDownLatch startSignal, CountDownLatch doneSignal,String threadName) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
		this.threadName = threadName;
	}

	public void run() {
		try {
			logger.info(threadName+"开始等待startSignal.await执行前...startSignal.getCount()"+startSignal.getCount());
			startSignal.await();
			logger.info(threadName+"startSignal.await执行后...startSignal.getCount()"+startSignal.getCount());
			doWork(threadName);
			logger.info(threadName+"doneSignal.countDown()执行前..."+doneSignal.getCount());
			doneSignal.countDown();
			logger.info(threadName+"doneSignal.countDown()执行后..."+doneSignal.getCount());
		} catch (InterruptedException ex) {
			logger.error("Worker run method error",ex);
		} // return;
	}

	void doWork(String threadName) {
		try {
			logger.info(threadName+"开始工作");
			Thread.sleep((new Random().nextInt(10)+1) * 1000);
			logger.info(threadName+"结束工作");
		} catch (InterruptedException e) {
			logger.error("Worker doWork method error",e);
		}
	}
}

public class CountDownLatchTest {
	static Logger logger = LoggerFactory.getLogger(CountDownLatchTest.class);

	public static final int N = 3;

	public static void main(String[] args) {
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);

		for (int i = 0; i < N; ++i)
			// create and start threads
			new Thread(new Worker(startSignal, doneSignal, "ctl" + i)).start();
        logger.info(N+"个进程启动完毕");
		doSomethingElseS(); // don't let run yet
		logger.info("startSignal.countDown()执行前...startSignal.getCount()"+startSignal.getCount());
		startSignal.countDown(); // let all threads proceed
		logger.info("startSignal.countDown()执行后...startSignal.getCount()"+startSignal.getCount());
		//doSomethingElseE();
		try {
			logger.info("doneSignal.await()执行前...startSignal.getCount()"+startSignal.getCount());
			doneSignal.await();
			logger.info("doneSignal.await()执行后...startSignal.getCount()"+startSignal.getCount());
		} catch (InterruptedException e) {
			logger.error("await异常",e);
		} // wait for all to finish
		logger.info("all over");
	}
	public static void doSomethingElseS(){
		try{
			logger.info("startSignal.countDown()执行前暂停3秒");
			Thread.sleep(3000);
		}catch (Exception e){

		}
	}
	public static void doSomethingElseE(){
		try{
			logger.info("startSignal.await()执行前暂停5秒");
			Thread.sleep(5000);
		}catch (Exception e){

		}
	}
}
