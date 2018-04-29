package com.ctl.utils.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-19下午8:25:25
 * @package_name com.ctl.util.concurrent
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description
 */

public class LiftOff implements Runnable {

	protected int countDown = 10;
	private static int taskCount = 0;
	private final int id = taskCount++;

	public LiftOff() {

	}

	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	public String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "LiftOff! ") + ")";
	}

	@Override
	public void run() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		while (countDown-- > 0) {
			System.out.print(status());
			Thread.yield();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 5; i++) {
			exec.execute(new LiftOff());
		}
		exec.shutdown();
	}
}