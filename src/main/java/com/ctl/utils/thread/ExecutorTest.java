package com.ctl.utils.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorTest {
	public static void main(String[] args) {
		// execute 执行器利用 工厂方法生产对象
		// 该线程池最多有三个线程
		Executor executor = Executors.newFixedThreadPool(3);
		executor.execute(new Dog("金毛"));
		executor.execute(new Dog("哈士奇"));
		executor.execute(new Dog("太迪"));
		executor.execute(new Dog("阿拉斯加"));
		executor.execute(new Dog("豆豆"));
		executor.execute(new Dog("毛毛"));
		executor.execute(new Dog("可可"));
		executor.execute(new Dog("爱爱"));
	}
}

class Dog implements Runnable {
	String name;

	public Dog(String name) {
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println(name + "  在跑   \t" + i);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.err.println("InterruptedException");
			}
		}
		System.out.println(name+"over");
	}

}