package com.ctl.utils.cache;

import com.ctl.utils.DateUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-19下午11:07:34
 * @package_name com.ctl.util.cache
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description
 */

public class EhcacheTest {
    static Logger logger = LoggerFactory.getLogger(EhcacheTest.class);

    /**@author 			guolin
     * @date    		2016-2-19下午11:07:34
     * @package_name 	com.ctl.util.cache
     * @project_name   	ctlUtils
     * @version 		version.1.0
     * @description
     * @param args
     */
    public static void main(String[] args) {
        CacheManager cacheManager = CacheManager.create();
        // 或者
        //cacheManager = CacheManager.getInstance();
        // 或者
        cacheManager =  CacheManager.create(EhcacheTest.class.getClassLoader().getResourceAsStream("ehcache.xml"));
        // 或者
        //	cacheManager = CacheManager.create("http://localhost:8080/test/ehcache.xml");
        //cacheManager = CacheManager.newInstance("/config/ehcache.xml");
        // .......

        // 获取ehcache配置文件中的一个cache
        Cache sample = cacheManager.getCache("SimplePageCachingFilter");
        // 获取页面缓存
        BlockingCache cache = new BlockingCache(cacheManager.getEhcache("SimplePageCachingFilter"));
        // 添加数据到缓存中
        Element element = new Element("key", "date"+ DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(new Date()));
        sample.put(element);
        // 获取缓存中的对象，注意添加到cache中对象要序列化 实现Serializable接口
        Element result = sample.get("key");
        logger.info(result.getObjectValue().toString());
        // 删除缓存
        sample.remove("key");
        sample.removeAll();

        // 获取缓存管理器中的缓存配置名称
        for (String cacheName : cacheManager.getCacheNames()) {
            logger.info(cacheName);
        }
        // 获取所有的缓存对象
        for (Object key : cache.getKeys()) {
            logger.info(key.toString());
        }

        // 得到缓存中的对象数
        logger.info("得到缓存中的对象数:"+cache.getSize());
        // 得到缓存对象占用内存的大小
        logger.info("得到缓存对象占用内存的大小:"+cache.getMemoryStoreSize());
        // 得到缓存读取的命中次数
        logger.info("得到缓存读取的命中次数:"+cache.getStatistics().getCacheHits());
        // 得到缓存读取的错失次数
        logger.info("得到缓存读取的错失次数:"+cache.getStatistics().getCacheMisses());
    }

}
