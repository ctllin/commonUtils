package com.ctl.utils.concurrent;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-19����8:11:54
 * @package_name com.ctl.util.concurrent
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description	 
 */

class TaskWithResult implements Callable<String> {
	private int id;

	public TaskWithResult(int id) {
		this.id = id;
	}

	@Override
	public String call() throws Exception {
		return "result of TaskWithResult " + id;
	}
}

public class CallableTest {
	public static void main(String[] args) throws InterruptedException,ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();	//Future 相当于是用来存放Executor执行的结果的一种容器
		for (int i = 0; i < 10; i++) {
			results.add(exec.submit(new TaskWithResult(i)));
		}
		for (Future<String> fs : results) {
			if (fs.isDone()) {
				System.out.println(fs.get());
			} else {
				System.out.println("Future result is not yet complete");
			}
		}
		exec.shutdown();
	}
}
