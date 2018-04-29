package com.ctl.utils.proxy.spring2;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:40:03
 * @package_name com.ctl.proxy.spring
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description JDK的动态代理机制只能代理实现了接口的类，而不能实现接口的类就不能实现JDK的动态代理，cglib是针对类来实现代理的，他的原理是对指定的目标类生成一个子类，并覆盖其中方法实现增强，但因为采用的是继承，所以不能对final修饰的类进行代理。
 */

public class TestCglib {
	public static void main(String[] args) {
		BookFacadeCglib cglib = new BookFacadeCglib();
		BookFacadeImpl bookCglib = (BookFacadeImpl) cglib.getInstance(new BookFacadeImpl());
		bookCglib.addBook();
	}
}