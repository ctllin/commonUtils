package com.ctl.utils.redis;

/**
 * <p>Title: RedisTool</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-01-08 13:34
 */

import com.ctl.utils.ConfigUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * redis实现分布式锁，并释放锁
 */
public class RedisTool {
    private static JedisCluster cluster  = null;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    static {
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
            cluster = new JedisCluster(nodes, 100000, 20000, 5, ConfigUtils.getType("redis.auth"), config);
        }
    }

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间,毫秒
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(JedisCluster jedis, String lockKey, String requestId, int expireTime) {
        /*
         *设置锁并设置超时时间，lockKey表示Redis key，requestId表示Redis value，SET_IF_NOT_EXIST表示有值不进行设置（NX），
         * SET_WITH_EXPIRE_TIME表示是否设置超时时间（PX）设置，expireTime表示设置超时的毫秒值
         * */
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(JedisCluster jedis, String lockKey, String requestId) {

        /*
         * 利用Lua脚本代码，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）
         * eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令，这样就不会出现上一个代码执行完挂了后边的出现问题，还是一致性的解决
         * */
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    public static void main(String[] args) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String lockKey = "lock_ctl";

        boolean resultLock = RedisTool.tryGetDistributedLock(cluster, lockKey, uuid, 300000);
        System.out.println(resultLock);
        boolean resultRelease =  RedisTool.releaseDistributedLock(cluster,lockKey,uuid);
        System.out.println(resultRelease);
        cluster.set("name","ctl");
        System.out.println(cluster.get("name"));
        cluster.close();

    }
}
