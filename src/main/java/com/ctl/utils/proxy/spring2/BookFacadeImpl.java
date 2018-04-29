package com.ctl.utils.proxy.spring2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guolin
 * @tel 18515287139
 * @date 2015-8-5上午9:31:53
 * @package_name com.ctl.proxy.jdk
 * @project_name Spring3.0企业应用开发实战
 * @version version.1.0
 * @description
 */
public class BookFacadeImpl {
	private static Logger logger = LoggerFactory.getLogger(BookFacadeImpl.class);
	public void addBook() {
		logger.info("增加图书方法。。。");
	}
}