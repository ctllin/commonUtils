package com.ctl.utils.redis;

import com.ctl.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private static Jedis jedis=redisInstances();
    private static JedisPool pool = null;
    private static Map<String, String> setDataMap = new HashMap<>();
    /**
     * 构建redis连接池
     * @return JedisPool
     */
    public static JedisPool getPool() {
        try {
            if (pool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
                //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
                config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
                //如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
                config.setMaxTotal(50);
                //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
                config.setMaxIdle(5);
                //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
                config.setMaxWaitMillis(1000 * 100);
                //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
                config.setTestOnBorrow(true);
                //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
                //config.setMaxWaitMillis(-1);
                pool = new JedisPool(config, ConfigUtils.getType("redis.host"), Integer.parseInt(ConfigUtils.getType("redis.port")),10000,ConfigUtils.getType("redis.auth"));
            }
            return pool;
        } catch (Exception e) {
            logger.error("获取redis链接池失败", e);
            return null;
        }
    }
    /**
     * 获取redis链接
     * @return
     */
    private static Jedis redisInstances(){
        if (jedis != null) {
            return jedis;
        } else {
            try {
                Jedis jedis = new Jedis(ConfigUtils.getType("redis.host"), Integer.parseInt(ConfigUtils.getType("redis.port")));
                jedis.auth(ConfigUtils.getType("redis.auth"));
                return jedis;
            } catch (Exception e) {
                logger.error("获取redis链接失败", e);
                return null;
            }
        }
    }

    /**
     * 关闭redis连接
     * @return
     */
    public static boolean close(){
        try {
            if (jedis != null) {
                jedis.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("关闭redis链接失败", e);
            return false;
        }
    }
    static { //初始化redis获取redis对象
        redisInstances();
    }

    /**
     * 根据会员id和商户id获取securityKey
     * @param memberId
     * @param merchantId
     * @return
     */
    public static String getSecurityKey(String memberId, String merchantId){
        try {
            List<String> mget = jedis.hmget("member-securityKey", memberId + "-" + merchantId);
            if(mget!=null && mget.size()>=1){
                return mget.get(0);
            }else{
                return null;
            }
        } catch (Exception e) {
            logger.error("获取securityKey失败",e);
            return null;
        }
    }

    /**
     * 根据会员id和商户id增加securityKey
     * @param memberId
     * @param merchantId
     * @return
     */
    public static String addSecurityKey(String memberId, String merchantId){
        try {
            //先获取如果存在直接返回,否则生成
            String securityKey = getSecurityKey(memberId, merchantId);
            if (securityKey == null) {
                securityKey = UUID.randomUUID().toString().replaceAll("-", "");
                setDataMap.put(memberId + "-" + merchantId, UUID.randomUUID().toString().replaceAll("-", ""));
                jedis.hmset("member-securityKey", setDataMap);
                setDataMap.clear();
                return securityKey;
            } else {
                return securityKey;
            }
        } catch (Exception e) {
            logger.error("增加securityKey失败",e);
            return null;
        }
    }

    /**
     * 根据会员id和商户id刷新securityKey
     * @param memberId
     * @param merchantId
     * @return
     */
    public  static String flushSecurityKey(String memberId, String merchantId){
        try {
            String securityKey = UUID.randomUUID().toString().replaceAll("-", "");
            setDataMap.put(memberId + "-" + merchantId, UUID.randomUUID().toString().replaceAll("-", ""));
            jedis.hmset("member-securityKey", setDataMap);
            setDataMap.clear();
            return securityKey;
        } catch (Exception e) {
            logger.error("刷新securityKey失败",e);
            return null;
        }
    }


    /**
     * 从指定map中获取数据
     * @param name map的名字
     * @param key
     * @return
     */
    public static String getFromMap(String name, String key) {
        try {
            List<String> mget = jedis.hmget(name, key);
            if (mget != null && mget.size() >= 1) {
                return mget.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("从指定map中获取数据失败", e);
            return null;
        }
    }

    /**
     * 从指定map中获取数据
     * @param name map的名字
     * @param key 多个key
     * @return
     */
    public static  List<String> getFromMap(String name, String ...key) {
        try {
            List<String> mget = jedis.hmget(name, key);
            return mget;
        } catch (Exception e) {
            logger.error("从指定map中获取数据失败", e);
            return null;
        }
    }

    /**
     * 向指定map中增加数据
     * @param name map的名字
     * @param key
     * @param value
     * @return
     */
    public static String addMapData(String name, String key, String value) {
        try {
            //先获取如果存在直接返回,否则生成
            String valueFromRedis = getFromMap(name, key);
            if (valueFromRedis == null) {
                setDataMap.put(key, value);
                jedis.hmset(name, setDataMap);
                setDataMap.clear();
                return value;
            } else {
                return valueFromRedis;
            }
        } catch (Exception e) {
            logger.error("向指定map中增加数据失败", e);
            return null;
        }
    }

    /**
     * 向指定map中增加数据
     * @param name map的名字
     * @param dataMap 需要增加的数据
     * @return
     */
    public static boolean addMapData(String name, Map<String, String> dataMap) {
        try {
            jedis.hmset(name, dataMap);
            return true;
        } catch (Exception e) {
            logger.error("向指定map中增加数据失败", e);
            return false;
        }
    }



    /**
     * 更新指定map中数据
     * @param name map的名字
     * @param key
     * @param value
     * @return
     */
    public static String flushMapData(String name, String key, String value) {
        try {
            setDataMap.put(key, value);
            jedis.hmset(name, setDataMap);
            setDataMap.clear();
            return value;
        } catch (Exception e) {
            logger.error(" 更新指定map中数据失败", e);
            return null;
        }
    }

    /**
     * 更新指定map中数据
     * @param name map的名字
     * @param dataMap 需要刷新的数据
     * @return
     */
    public static boolean flushMapData(String name, Map<String, String> dataMap) {
        try {
            jedis.hmset(name, dataMap);
            return true;
        } catch (Exception e) {
            logger.error(" 更新指定map中数据失败", e);
            return false;
        }
    }



    public static void main(String[] args) {
        System.out.println(        getPool().getResource().hmget("test1","ctl","kym","zxp"));
        String memberId="2";
        String merchantId="8";
        addSecurityKey(memberId,merchantId);
        System.out.println(getSecurityKey(memberId,merchantId));
        System.out.println(flushSecurityKey(memberId,merchantId));
        addMapData("ctl","age"+UUID.randomUUID(),"25");
        System.out.println(getFromMap("ctl","age"+UUID.randomUUID()));
        System.out.println(flushMapData("ctl","age","27"+UUID.randomUUID()));
        Map<String ,String > map =new HashMap<>();
        map.put("ctl","26");
        map.put("kym","27");
        System.out.println(addMapData("test1",map));
        map.put("ctl","27");
        map.put("kym","28");
        map.put("zxp","28");
        System.out.println(getFromMap("test1","ctl","kym","zxp"));
        flushMapData("test1",map);
        System.out.println(getFromMap("test1","ctl","kym","zxp"));
        close();
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
