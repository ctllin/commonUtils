package com.ctl.utils.io;

/**
 * @author guolin
 * @date 2015-11-4下午3:33:27
 * @package_name NetFox
 * @project_name ctlUtils
 * @version version.1.0
 */
public class Utility {
	public Utility() {
	}

	public static void sleep(int nSecond) {
		try {
			Thread.sleep(nSecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(String sMsg) {
		System.err.println(sMsg);
	}

	public static void log(long sMsg) {
		System.err.println(sMsg);
	}
}
