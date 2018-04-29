package com.ctl.utils;
import java.util.Random;

public class DataUtils {
	private static Random random = new Random();
	private static final int MAX_NUMBER = 100;

	/**
	 * 	\u968F\u673A\u57280\u5230100\u95F4\u53D6\u6570
	 * @return 
	 */
	public static int getRandomData() {
		return random.nextInt(MAX_NUMBER);
	}
	public static int getRandomData0To99() {
		return random.nextInt(MAX_NUMBER);
	}
}
