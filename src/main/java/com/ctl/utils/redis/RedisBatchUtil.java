package com.ctl.utils.redis;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.util.*;

/**
 * <p>Title: RedisBatchUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-04-01 09:59
 */
public class RedisBatchUtil {
    static final Logger logger = LoggerFactory.getLogger(RedisBatchUtil.class);

    /**
     * 功能描述 批量HgetAll
     *
     * @param jc   JedisCluster对象
     * @param keys key
     * @name mHgetAll  awifi_
     * @return: java.util.Map<java.lang.String               ,               java.lang.Object>
     */
    public static Map<String, Map<String, String>> mHgetAll(JedisCluster jc, List<String> keys) {
        logger.debug("{}", keys.size());
        long startTime = System.currentTimeMillis();

        if (keys == null || keys.size() == 0) {
            return new HashMap<>(0);
        }
        Map<String, Map<String, String>> resMap = new HashMap<>();
        //如果只有一条，直接使用get即可
        if (keys.size() == 1) {
            resMap.put(keys.get(0), jc.hgetAll(keys.get(0)));
            return resMap;
        }
        Map<JedisPool, List<String>> jedisPoolMap = getJedisPoolMap(jc, keys);
        //保存结果
        List<Object> res;
        //执行
        for (Map.Entry<JedisPool, List<String>> entry : jedisPoolMap.entrySet()) {
            Jedis jedis = null;
            try {
                JedisPool currentJedisPool = entry.getKey();
                List<String> keyList = entry.getValue();
                jedis = currentJedisPool.getResource();
                // 获取pipeline
                Pipeline currentPipeline = jedis.pipelined();
                for (String key : keyList) {
                    currentPipeline.hgetAll(key);
                }
                // 从pipeline中获取结果
                res = currentPipeline.syncAndReturnAll();
                currentPipeline.close();
                for (int i = 0; i < keyList.size(); i++) {
                    resMap.put(keyList.get(i), res.get(i) == null ? new HashMap<>(0) : (Map<String, String>) res.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new HashMap<>(0);
            } finally {
                returnResource(jedis);
            }

        }
        long entTime = System.currentTimeMillis();
        logger.debug("RedisBatch_HgetAll time:{}", entTime - startTime);
        return resMap;
    }

    /**
     * 功能描述 批量Hget
     *
     * @param jc   JedisCluster对象
     * @param keys key
     * @name mHgetAll
     * @return: java.util.Map<java.lang.String               ,               java.lang.Object>
     */
    public static Map<String, String> mHget(JedisCluster jc, List<String> keys, String field) {
        long startTime = System.currentTimeMillis();

        if (keys == null || keys.size() == 0) {
            logger.debug("批量Hget参数keys为空");
            return new HashMap<>(0);
        }
        Map<String, String> resMap = new HashMap<>();
        //如果只有一条，直接使用get即可
        if (keys.size() == 1) {
            logger.debug("批量Hget参数keys为1");
            resMap.put(keys.get(0), jc.hget(keys.get(0), field));
            return resMap;
        }
        Map<JedisPool, List<String>> jedisPoolMap = getJedisPoolMap(jc, keys);
        //保存结果
        List<Object> res;
        //执行
        for (Map.Entry<JedisPool, List<String>> entry : jedisPoolMap.entrySet()) {
            Jedis jedis = null;
            try {
                JedisPool currentJedisPool = entry.getKey();
                List<String> keyList = entry.getValue();
                jedis = currentJedisPool.getResource();
                // 获取pipeline
                Pipeline currentPipeline = jedis.pipelined();
                for (String key : keyList) {
                    currentPipeline.hget(key, field);
                }
                // 从pipeline中获取结果
                res = currentPipeline.syncAndReturnAll();
                currentPipeline.close();
                for (int i = 0; i < keyList.size(); i++) {
                    resMap.put(keyList.get(i), res.get(i) == null ? null : (String) res.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new HashMap<>(0);
            } finally {
                returnResource(jedis);
            }
        }
        long entTime = System.currentTimeMillis();
        logger.debug("RedisBatch_Hget time:{}", entTime - startTime);
        return resMap;
    }

    /**
     * 批量更新数据
     *
     * @param jc
     * @param hash
     * @return
     */
    public static long hmset(JedisCluster jc, Map<String, Map<String, String>> hash) {
        logger.info("hash大小:{}", hash.size());
        if (hash == null || hash.size() == 0) {
            return 0L;
        }
        if (hash.size() == 1) {
            long count = hash.entrySet().stream().filter(map -> {
                if (map.getValue() != null && map.getKey() != null && map.getValue().size() > 0) {
                    String hmset = jc.hmset(map.getKey(), map.getValue());
                    return true;
                }
                return false;
            }).count();
            return count;
        }
        //获取key, 获取地址+端口和命令的映射
        Map<JedisPool, List<String>> jedisPoolMap = getJedisPoolMap(jc, new ArrayList<String>(hash.keySet()));
        long count = 0L;
        //执行
        for (Map.Entry<JedisPool, List<String>> entry : jedisPoolMap.entrySet()) {
            Jedis jedis = null;
            try {
                JedisPool currentJedisPool = entry.getKey();
                List<String> keyList = entry.getValue();
                jedis = currentJedisPool.getResource();
                // 获取pipeline
                Pipeline currentPipeline = jedis.pipelined();
                for (String key : keyList) {
                    Map<String, String> mapHash = hash.get(key);
                    if (mapHash != null && mapHash.size() > 0) {
                        currentPipeline.hmset(key, hash.get(key));
                        count++;
                    } else {
                        logger.warn("key:{}的值为Null", key);
                    }
                }
                // 从pipeline中获取结果
                currentPipeline.syncAndReturnAll();
                currentPipeline.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                returnResource(jedis);
            }
        }
        logger.info("redis批量更新数量:{}", count);
        return count;
    }

    /**
     * 获取地址+端口和命令的映射
     *
     * @param jc
     * @param keys
     * @return
     */
    private static Map<JedisPool, List<String>> getJedisPoolMap(JedisCluster jc, List<String> keys) {
        //JedisCluster继承了BinaryJedisCluster
        //BinaryJedisCluster的JedisClusterConnectionHandler属性
        //里面有JedisClusterInfoCache，根据这一条e继承链，可以获取到JedisClusterInfoCache
        //从而获取slot和JedisPool直接的映射
        MetaObject metaObject = SystemMetaObject.forObject(jc);
        JedisClusterInfoCache cache = (JedisClusterInfoCache) metaObject.getValue("connectionHandler.cache");

        //保存地址+端口和命令的映射
        Map<JedisPool, List<String>> jedisPoolMap = new HashMap<>();

        List<String> keyList = null;
        JedisPool currentJedisPool = null;

        for (String key : keys) {
            //计算哈希槽
            int crc = JedisClusterCRC16.getSlot(key);
            //通过哈希槽获取节点的连接
            currentJedisPool = cache.getSlotPool(crc);
            //由于JedisPool作为value保存在JedisClusterInfoCache中的一个map对象中，每个节点的
            //JedisPool在map的初始化阶段就是确定的和唯一的，所以获取到的每个节点的JedisPool都是一样
            //的，可以作为map的key
            if (jedisPoolMap.containsKey(currentJedisPool)) {
                jedisPoolMap.get(currentJedisPool).add(key);
            } else {
                keyList = new ArrayList<>();
                keyList.add(key);
                jedisPoolMap.put(currentJedisPool, keyList);
            }
        }
        return jedisPoolMap;
    }

    /**
     * 返还到连接池
     *
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            logger.debug("jedis返还到连接池");
            jedis.close();
        }
    }

    /**
     * redis 的keys功能
     *
     * @param jedisCluster
     * @param pattern      匹配字段
     * @return
     */
    public static TreeSet<String> keys(JedisCluster jedisCluster, String pattern) {
        logger.debug("Start getting keys...");
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            logger.debug("Getting keys from: {}", entry.getKey());
            JedisPool jp = clusterNodes.get(entry.getKey());
            Jedis connection = null;
            try {
                connection = jp.getResource();
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                logger.error("Getting keys error: {}", e);
            } finally {
                logger.debug("Connection closed.");
                returnResource(connection);
                //用完一定要close这个链接！！！
            }
        }
        logger.debug("Keys gotten!");
        return keys;
    }

    public static void main(String[] args) {
        int slot = JedisClusterCRC16.getSlot("scanShoppingConfig-1-215");
        System.out.println(slot);
    }
}
