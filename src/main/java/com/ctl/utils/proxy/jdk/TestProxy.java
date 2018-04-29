package com.ctl.utils.proxy.jdk;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:30:06
 * @package_name com.ctl.proxy.jdk
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description JDK的动态代理依靠接口实现，如果有些类并没有实现接口，则不能使用JDK代理，这就要使用cglib动态代理了
 */

public class TestProxy {
	public static void main(String[] args) {
		BookFacadeProxy proxy = new BookFacadeProxy();
		BookFacade bookProxy = (BookFacade) proxy.bind(new BookFacadeImpl());
		bookProxy.addBook();
	}
}