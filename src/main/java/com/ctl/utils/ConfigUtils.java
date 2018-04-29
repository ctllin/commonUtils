package com.ctl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.*;
public class ConfigUtils {
	private static Properties config;
	private static InputStream is;
	static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
	static {
		config = new Properties();
		is = ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			config.load(is);
			is.close();
		} catch (IOException e) {
			logger.error("初始化资源文件失败:",e);
		}finally{
			
		}
	}
	public static boolean flushProperties(){
		is = ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			config.load(is);
			is.close();
			return true;
		} catch (IOException e) {
			logger.error("初始化资源文件失败:",e);
		}finally{
			
		}
		return false;
	}
	public static String getType(String type) {
		return config.getProperty(type);
	}

	/**
	 * @return \u8FD4\u56DE\u6240\u6709\u7684key\u548Cvalue
	 * @deprecated \u8FD4\u56DE\u7684\u7C7B\u578B\u662FHashMap<String,String>
	 */
	public static HashMap<String, String> getProperties() {
		HashMap<String, String> map = new HashMap<String, String>();
		Set<Object> set = new HashSet<Object>();
		set = config.keySet();
		Iterator<Object> iter = set.iterator();
		String key;
		for (; iter.hasNext();) {
			key = iter.next().toString();
			map.put(key, ConfigUtils.getType(key));
		}
		return map;
	}

	/**
	 * @deprecated \u8FD4\u56DE\u7C7B\u578B\u662F HashSet
	 *             \u6240\u6709\u7684\u8BE5\u8D44\u6E90\u6587\u4EF6\u4E2D\u6240\u6709\u7684key
	 *             (Object)
	 */
	public static Set<Object> getAllKeys() {
		Set<Object> set = new HashSet<Object>();
		set = config.keySet();
		return set;
	}

	/**
	 * @deprecated \u8FD4\u56DE\u7C7B\u578B\u662F HashSet
	 *             \u6240\u6709\u7684\u8BE5\u8D44\u6E90\u6587\u4EF6\u4E2D\u6240\u6709\u7684value
	 *             (Object)
	 */
	public static Set<Object> getAllValues() {
		Set<Object> set = new HashSet<Object>();
		Set<Object> setReturtn = new HashSet<Object>();
		set = config.keySet();
		Iterator<Object> iter = set.iterator();
		String key;
		for (; iter.hasNext();) {
			key = iter.next().toString();
			setReturtn.add(ConfigUtils.getType(key));
		}
		return setReturtn;
	}

	/**
	 * @param key
	 *            \u8981\u5224\u65AD\u7684key
	 * @return \u5B58\u5728\u8FD4\u56DEtrue\u4E0D\u5B58\u5728\u8FD4\u56DEfalse
	 */
	public static boolean containKey(String key) {
		return ConfigUtils.getAllKeys().contains(key);
	}

	private static Properties getConfig() {
		return config;
	}
	public static void main(String[] args) {
		String name=getType("name");
		logger.info(name);
		logger.info(getType("age"));
	}
}
