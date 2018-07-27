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
    //private static Jedis jedis=redisInstances();
    private static JedisPool pool = null;
    private static Map<String, String> setDataMap = new HashMap<>();
    /**
     * 构建redis连接池 使用过得redis连接直接调用close方法关闭(使用后必须关闭不可以使用pool.returnResource(redis))
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
//    private static Jedis redisInstances(){
//        if (jedis != null) {
//            return jedis;
//        } else {
//            try {
//                Jedis jedis = new Jedis(ConfigUtils.getType("redis.host"), Integer.parseInt(ConfigUtils.getType("redis.port")));
//                jedis.auth(ConfigUtils.getType("redis.auth"));
//                return jedis;
//            } catch (Exception e) {
//                logger.error("获取redis链接失败", e);
//                return null;
//            }
//        }
//    }

    /**
     * 关闭redis连接
     * @return
     */
//    public static boolean close(){
//        try {
//            if (jedis != null) {
//                jedis.close();
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            logger.error("关闭redis链接失败", e);
//            return false;
//        }
//    }
    static { //初始化redis获取redis对象
        //redisInstances();
    }

    /**
     * 根据会员id和商户id获取securityKey
     * @param merchantId
     * @param memberId
     * @return
     */
    public static String getSecurityKey(String merchantId, String memberId) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            List<String> mget = redis.hmget("member-securityKey", merchantId + "-" + memberId);
            if (mget != null && mget.size() >= 1) {
                return mget.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("获取securityKey失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    /**
     * 根据appid和商户id获取sessionKey
     * @param merchantId
     * @param appid
     * @param openid
     * @return
     */
    public static String getSessionKey(String merchantId, String appid, String openid) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            List<String> mget = redis.hmget("member-sessionKey", merchantId + "-" + appid + "-" + openid);
            if (mget != null && mget.size() >= 1) {
                return mget.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("获取sessionKey失败",e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据商户id和appid获取appsecret
     * @param merchantId
     * @param appid
     * @return
     */
    public static String getSmallAppsecret(String merchantId, String appid) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            List<String> mget = redis.hmget("member-appsecret", merchantId.trim() + "-" + appid.trim());
            if (mget != null && mget.size() >= 1) {
                return mget.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("获取appsecret失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据会员id和商户id增加securityKey
     * @param merchantId
     * @param memberId
     * @return
     */
    public static String addSecurityKey(String merchantId, String memberId) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            //先获取如果存在直接返回,否则生成
            String securityKey = getSecurityKey(merchantId, memberId);
            if (securityKey == null) {
                securityKey = UUID.randomUUID().toString().replaceAll("-","");
                setDataMap.put(merchantId + "-" + memberId, securityKey);
                redis.hmset("member-securityKey", setDataMap);
                setDataMap.clear();
                return securityKey;
            } else {
                return securityKey;
            }
        } catch (Exception e) {
            logger.error("增加securityKey失败",e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    /**
     * 根据商户id和appid增加appsecret
     * @param merchantId
     * @param appid
     * @param appsecret
     * @return
     */
    public static String addAppsecret(String merchantId, String appid, String appsecret) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            if (appsecret != null && !"".equals(appsecret)) {
                setDataMap.put(merchantId + "-" + appid, appsecret);
                redis.hmset("member-appsecret", setDataMap);
                setDataMap.clear();
                return appsecret;
            } else {
                return appsecret;
            }
        } catch (Exception e) {
            logger.error("增加appsecret失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    /**
     * 根据appid和商户id增加sessionKey
     * @param merchantId
     * @param appid
     * @param openid
     * @return
     */
    public static String addSessionKey( String merchantId,String appid, String openid, String sessionKey) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            if (sessionKey != null && !"".equals(sessionKey)) {
                setDataMap.put(merchantId + "-"+appid+"-" + openid, sessionKey);
                redis.hmset("member-sessionKey", setDataMap);
                setDataMap.clear();
                return sessionKey;
            } else {
                return sessionKey;
            }
        } catch (Exception e) {
            logger.error("增加sessionKey失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    /**
     * 根据会员id和商户id刷新securityKey
     * @param merchantId
     * @param memberId
     * @return
     */
    public  static String flushSecurityKey(String merchantId, String memberId){
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            String securityKey = UUID.randomUUID().toString().replaceAll("-","");
            setDataMap.put(merchantId + "-" + memberId, securityKey);
            redis.hmset("member-securityKey", setDataMap);
            setDataMap.clear();
            return securityKey;
        } catch (Exception e) {
            logger.error("刷新securityKey失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    /**
     * 根据appid和商户id刷新sessionKey
     * @param appid
     * @param merchantId
     * @return
     */
    public static String flushSessionKey(String merchantId, String appid, String openid, String sessionKey) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            if (sessionKey != null && !"".equals(sessionKey)) {
                setDataMap.put(merchantId + "-" + appid + "-" + openid, sessionKey);
                redis.hmset("member-sessionKey", setDataMap);
                setDataMap.clear();
                return sessionKey;
            } else {
                return sessionKey;
            }
        } catch (Exception e) {
            logger.error("刷新sessionKey失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 从指定map中获取数据
     * @param name map的名字
     * @param key
     * @return
     */
    public static String getFromMap(String name, String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            List<String> mget = redis.hmget(name, key);
            if (mget != null && mget.size() >= 1) {
                return mget.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("从指定map中获取数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 从指定map中获取数据
     * @param name map的名字
     * @param key 多个key
     * @return
     */
    public static  List<String> getFromMap(String name, String ...key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            List<String> mget = redis.hmget(name, key);
            return mget;
        } catch (Exception e) {
            logger.error("从指定map中获取数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
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
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            //先获取如果存在直接返回,否则生成
            String valueFromRedis = getFromMap(name, key);
            if (valueFromRedis == null) {
                setDataMap.put(key, value);
                redis.hmset(name, setDataMap);
                setDataMap.clear();
                return value;
            } else {
                return valueFromRedis;
            }
        } catch (Exception e) {
            logger.error("向指定map中增加数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 向指定map中增加数据
     * @param name map的名字
     * @param dataMap 需要增加的数据
     * @return
     */
    public static boolean addMapData(String name, Map<String, String> dataMap) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            redis.hmset(name, dataMap);
            return true;
        } catch (Exception e) {
            logger.error("向指定map中增加数据失败", e);
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
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
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            setDataMap.put(key, value);
            redis.hmset(name, setDataMap);
            setDataMap.clear();
            return value;
        } catch (Exception e) {
            logger.error(" 更新指定map中数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据map名称获取所有map数据
     * @param name map名称
     * @return 有则返回map没有则返回空map
     */
    public static Map<String, String> getFromMap(String name) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.hgetAll(name);
        } catch (Exception e) {
            logger.error("根据map名称获取所有map数据", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 更新指定map中数据
     * @param name map的名字
     * @param dataMap 需要刷新的数据
     * @return
     */
    public static boolean flushMapData(String name, Map<String, String> dataMap) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            redis.hmset(name, dataMap);
            return true;
        } catch (Exception e) {
            logger.error(" 更新指定map中数据失败", e);
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
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

    /**
     * 设置字符串 一直存在
     * @param key
     * @param value
     * @return
     */
    public static boolean setString(String key,String value) {
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

    /**
     * 根据key删除字符串
     * @param key
     * @return 成功返回1失败返回0异常返回-1(没有该key删除返回0)
     */
    public static long delString(String key) {
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

    /**
     * 获取字符串
     * @param key
     * @return
     */
    public static String getString(String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            redis.get(key);
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

    /**
     * 存储list
     * @param key
     * @param value 多个value值
     * @return 返回list集合数量
     */
    public static long setList(String key ,String... value){
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            long lset = redis.lpush(key,value);
            return lset;
        } catch (Exception e) {
            logger.error("存储list失败", e);
            return 0;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据key从集合中获取数据,该数据会从list中删除,最后加入的先pop(删除)
     * @param key
     * @return
     */
    public static String getFromList(String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.lpop(key);
        } catch (Exception e) {
            logger.error("根据key从集合中获取数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据key获取list长度
     * @param key
     * @return 如果list不存在返回0,如果获取出错返回-1
     */
    public static long getSizeList(String key) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.llen(key);
        } catch (Exception e) {
            logger.error("根据key获取list长度失败", e);
            return -1;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据key,start,end 获取list数据
     * @param key list集合名称
     * @param start 从index=start开始获取(index 从0开始)
     * @param end 到index=end
     * @return 有则返回list结合没有则返回空集合([])
     */
    public static  List<String> getList(String key,long start,long end) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("根据key从集合中获取数据失败", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 获取list集合所有数据
     * @param listName
     * @return
     */
    public static  List<String> getList(String listName) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.lrange(listName, 0, redis.llen(listName));
        } catch (Exception e) {
            logger.error("获取list集合所有数据", e);
            return null;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据list名称删除list
     * @param listName
     * @return 返回删除list集合size大小,删除异常返回-1
     */
    public static long delList(String listName) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.expire(listName, 0);
        } catch (Exception e) {
            logger.error("根据list名称删除list失败", e);
            return -1;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 根据key名称删除数据
     * @param keyName
     * @return
     */
    public static boolean del(String keyName) {
        Jedis redis = null;
        try {
            redis = getPool().getResource();
            return redis.expire(keyName, 0)==1?true:false;
        } catch (Exception e) {
            logger.error("根据key名称删除数据失败", e);
            return false;
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }
    public static void main(String[] args) {

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
