package com.ctl.utils.proxy.staticproxy;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:24:34
 * @package_name com.ctl.proxy.staticproxy
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class TestCount {
	public static void main(String[] args) {
		CountImpl countImpl = new CountImpl();
		CountProxy countProxy = new CountProxy(countImpl);
		countProxy.updateCount();
		countProxy.queryCount();
	}
}