package com.ctl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: RedisUtil</p>
 * <p>Description: redis工具类 </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 * @author guolin
 * @version 1.0
 * @date 2018-05-17 19:57
 */
public class RedisUtil {
    static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    //private static Jedis jedis=redisInstances();
    private static JedisPool pool = null;
    private static JedisCluster cluster  = null;
    private static Map<String, String> setDataMap = new HashMap<>();
    private static final Integer SECURITY_KEY_EXPIRE_TIME=24*60*60*31;
    static {
        getPool();
    }
    private static void initializePool(){
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(100);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 50);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        //在将连接放回池中前，自动检验连接是否有效
        config.setTestOnReturn(true);
        //自动测试池中的空闲连接是否都是可用连接
        config.setTestWhileIdle(true);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        //config.setMaxWaitMillis(-1);
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            Set<HostAndPort> nodes = new LinkedHashSet<>();
            String redisClusterAddress = ConfigUtils.getType("redis.cluster.address");
            String[] redisAddress = redisClusterAddress.split(",");
            if (redisAddress != null) {
                for (int i = 0; i < redisAddress.length; i++) {
                    String redisAddressTemp = redisAddress[i];
                    String[] addressPort = redisAddressTemp.split(":");
                    if (addressPort != null && addressPort.length >= 2) {
                        nodes.add(new HostAndPort(addressPort[0], Integer.parseInt(addressPort[1])));//master
                    }
                }
            }
            //加入前面三个为master后面三个为从属当关闭前三个后 后三个会自动变master 然后从后三个获取数据
            cluster = new JedisCluster(nodes,100000,20000,30,ConfigUtils.getType("redis.auth"), config);
        } else {
            pool = new JedisPool(config, ConfigUtils.getType("redis.host"), Integer.parseInt(ConfigUtils.getType("redis.port")), 10000, ConfigUtils.getType("redis.auth"));
        }
        logger.info("redisType={},#1普通2集群",redisType);
    }
    /**
     * 多线程环境同步初始化（保证项目中有且仅有一个连接池）
     */
    private static synchronized void poolInit() {
        if (null == pool || cluster == null) {
            initializePool();
        }
    }

    /**
     * 构建redis连接池 使用过得redis连接直接调用close方法关闭(使用后必须关闭不可以使用pool.returnResource(redis))
     * @return JedisPool
     */
    private static JedisPool getPool() {
        try {
            if (pool == null || cluster == null) {
                poolInit();
            }
            if (pool != null) {
                logger.info("redis连接池参数如下：numActive={},numIdle={},numWaiters={}", pool.getNumActive(), pool.getNumIdle(), pool.getNumWaiters());
            }
            return pool;
        } catch (Exception e) {
            logger.error("获取redis链接池失败", e);
            return null;
        }
    }

    /**
     * 设置字符串 保存时间为制定时长
     * @param key
     * @param value
     * @param expire 保存时长单位秒
     * @return
     */
    public static boolean setString(String key,String value,int expire) {
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            return setStringCluster(key, value, expire);
        } else {
            return setStringSingle(key, value, expire);
        }
    }
    public static boolean setStringSingle(String key,String value,int expire) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            String returnStr = redis.set(key, value);
            redis.expire(key, expire);
            return "OK".equals(returnStr) ? true : false;
        } catch (Exception e) {
            logger.error("设置字符串失败", e);
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static boolean setStringCluster(String key,String value,int expire) {
        try {
            String returnStr = cluster.set(key, value);
            cluster.expire(key, expire);
            return "OK".equals(returnStr) ? true : false;
        } catch (Exception e) {
            logger.error("设置字符串失败", e);
            return false;
        } finally {

        }
    }


    /**
     * 设置字符串 一直存在
     * @param key
     * @param value
     * @return
     */
    public static boolean setString(String key,String value) {
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            return setStringCluster(key, value);
        } else {
            return setStringSingle(key, value);
        }
    }
    public static boolean setStringSingle(String key,String value) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            String returnStr = redis.set(key, value);
            return "OK".equals(returnStr) ? true : false;
        } catch (Exception e) {
            logger.error("设置字符串失败失败", e);
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static boolean setStringCluster(String key,String value) {
        try {
            String returnStr = cluster.set(key, value);
            return "OK".equals(returnStr) ? true : false;
        } catch (Exception e) {
            logger.error("设置字符串失败失败", e);
            return false;
        } finally {

        }
    }


    /**
     * 根据key删除字符串
     * @param key
     * @return 成功返回1失败返回0异常返回-1(没有该key删除返回0)
     */
    public static long delString(String key) {
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            return delStringCluster(key);
        } else {
            return delStringSingle(key);
        }
    }
    public static long delStringSingle(String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.expire(key, 0);
        } catch (Exception e) {
            logger.error("根据key删除字符串失败", e);
            return -1;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static long delStringCluster(String key) {
        try {
            return cluster.expire(key, 0);
        } catch (Exception e) {
            logger.error("根据key删除字符串失败", e);
            return -1;
        } finally {

        }
    }


    /**
     * 获取字符串
     * @param key
     * @return
     */
    public static String getString(String key) {
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            return getStringCluster(key);
        } else {
            return getStringSingle(key);
        }
    }
    public static String getStringSingle(String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.get(key);
        } catch (Exception e) {
            logger.error("获取字符串失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static String getStringCluster(String key) {
        try {
            return cluster.get(key);
        } catch (Exception e) {
            logger.error("获取字符串失败", e);
            try {
                return cluster.get(key);
            } catch (Exception e1) {
                logger.error("第二次获取字符串失败", e);
                return null;
            }
        } finally {

        }
    }

    /**
     * 根据key名称模糊获取key列表
     * @param pattern 例如member*
     * @return
     */
    public static Set<String> getKeySet(String pattern) {
        String redisType = ConfigUtils.getType("redis.type");
        if ("2".equals(redisType)) { //集群
            return getKeySetCluster(pattern);
        } else {
            return getKeySetSingle(pattern);
        }
    }
    public static Set<String> getKeySetSingle(String pattern) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.keys(pattern);
        } catch (Exception e) {
            logger.error("根据key名称删除数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static Set<String> getKeySetCluster(String pattern) {
        try {
            return cluster.hkeys(pattern);
        } catch (Exception e) {
            logger.error("根据key名称删除数据失败", e);
            try {
                return cluster.hkeys(pattern);
            } catch (Exception e1) {
                logger.error("第二次根据key名称删除数据失败", e);
                return null;
            }
        } finally {

        }
    }
    public static void main(String[] args) {
        Set<String> set = RedisUtil.getKeySet("member*");
        System.out.println(set);
        RedisUtil.setString("123","456");
        System.out.println(RedisUtil.getString("123"));

        for(int i=0;i<10000;i++){
           int j=i;
           new Thread(
                   new Runnable() {
                       @Override
                       public void run() {
                           System.out.println(setString(""+j,System.currentTimeMillis()+"", 100));
                           System.out.println(getString(j+""));
                       }
                   }
           ).start();
        }
    }

}
//<dependencies>
//<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
//<dependency>
//<groupId>redis.clients</groupId>
//<artifactId>jedis</artifactId>
//<version>2.9.0</version>
//</dependency>
//<dependency>
//<groupId>org.apache.commons</groupId>
//<artifactId>commons-pool2</artifactId>
//<version>2.4.2</version>
//</dependency>
//</dependencies>
