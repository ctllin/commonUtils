package com.ctl.utils;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class App 
{
	static Logger logger = LoggerFactory.getLogger(App.class); 

    public static void main( String[] args ) throws NoSuchAlgorithmException{
        System.out.println( "Hello World!" +StringUtils.leftPad("1",4, "0"));
        System.out.println(MD5Util.md5_32("admin").toUpperCase());
        logger.info("test");
        logger.debug("test");
    }
}
