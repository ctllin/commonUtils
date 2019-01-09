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
import redis.clients.jedis.params.SetParams;

import java.io.IOException;
import java.util.*;

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
            cluster = new JedisCluster(nodes, 1000, 1000, 5, ConfigUtils.getType("redis.auth"), config);
            System.out.println(Arrays.deepToString(nodes.toArray()));
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
        //String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
       try {
           SetParams params = new SetParams();
           params.ex(expireTime);
           System.out.println("getLock pre"+jedis.get(lockKey));
           String result = jedis.set(lockKey, requestId, params);
           if (LOCK_SUCCESS.equals(result)) {
               return true;
           }
           return false;
       }catch (Exception e){
           System.out.println(e);
           return false;
       }
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
        try {
            System.out.println("name=" + jedis.get("name"));
            System.out.println(jedis.get(lockKey));
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } catch (Exception e) { //当lockKey数据所在的master,服务异常后
            System.out.println(e);
            try {
                String lockValue = jedis.get(lockKey);
                System.out.println("第一次获取失败后第二次获取该锁" + lockValue);
                if (lockKey != null && !"".equals(lockKey)) { //当lockKey数据所在的master,服务异常后,获取lock,如果获取成功进行第二次释放锁操作
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
                    if (RELEASE_SUCCESS.equals(result)) {
                        return true;
                    }
                    return false;
                }
            } catch (Exception e1) {
                System.out.println("e1:" + e1);
            }
            return false;
        }

    }

    public static void main(String[] args) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String lockKey = "lock_ctl";

        boolean resultLock = RedisTool.tryGetDistributedLock(cluster, lockKey, uuid, 300000);
        System.out.println(0 + " 锁定 " + resultLock);
//        获取集群信息
//        192.168.42.29:6380> cluster nodes
//        942a9781eda2ab833e18ca1d3983e1a63917ed48 192.168.42.29:6380@16380 myself,master - 0 1547030154000 2 connected 5461-10922
//        2c69433fe67a7a71d99da46f132c1cbc0d688186 192.168.42.29:6382@16382 slave 942a9781eda2ab833e18ca1d3983e1a63917ed48 0 1547030156638 4 connected
//        8b7d4e667d7958d7a0beb2bc78f626a36836355c 192.168.42.29:6384@16384 slave 73ad4dd96bc69597fbe44e2f2dd44cb844e89897 0 1547030157545 20 connected
//        73ad4dd96bc69597fbe44e2f2dd44cb844e89897 192.168.42.29:6379@16379 master - 0 1547030156537 20 connected 0-5460
//        ff4ee9b42de3706f0b7fcdab4edd4a1646beb394 192.168.42.29:6381@16381 master - 0 1547030157644 16 connected 10923-16383
//        ac0f6d9e988c62849fb2032827e80b422edad514 192.168.42.29:6383@16383 slave ff4ee9b42de3706f0b7fcdab4edd4a1646beb394 0 1547030157000 16 connected
//        192.168.42.29:6380>

//        获取lockKey在哪个master上
//        192.168.42.29:6381> get lock_ctl
//                -> Redirected to slot [2942] located at 192.168.42.29:6379
//        "a3959719-137d-4ba1-8ffa-16e33467cbba"
        // 发现数据存储在192.168.42.29:6379这个master上将192.168.42.29:6379 杀死
//        192.168.42.29:6380> cluster nodes
//        942a9781eda2ab833e18ca1d3983e1a63917ed48 192.168.42.29:6380@16380 myself,master - 0 1547030369000 2 connected 5461-10922
//        2c69433fe67a7a71d99da46f132c1cbc0d688186 192.168.42.29:6382@16382 slave 942a9781eda2ab833e18ca1d3983e1a63917ed48 0 1547030369000 4 connected
//        8b7d4e667d7958d7a0beb2bc78f626a36836355c 192.168.42.29:6384@16384 master - 0 1547030370833 21 connected 0-5460
//        73ad4dd96bc69597fbe44e2f2dd44cb844e89897 192.168.42.29:6379@16379 master,fail - 1547030310724 1547030310000 20 disconnected
//        ff4ee9b42de3706f0b7fcdab4edd4a1646beb394 192.168.42.29:6381@16381 master - 0 1547030369527 16 connected 10923-16383
//        ac0f6d9e988c62849fb2032827e80b422edad514 192.168.42.29:6383@16383 slave ff4ee9b42de3706f0b7fcdab4edd4a1646beb394 0 1547030369828 16 connected

        //此时如果有另一个新的JedisCluster创建,是可以获取到到该锁,如果requestId正确也可以释放该锁，但是本JedisCluster在断开master后，无法获取到该锁,自然释放该锁失败,第二次获取或释放锁会成功
        boolean resultRelease =  RedisTool.releaseDistributedLock(cluster,lockKey,uuid);
        System.out.println(1 + " 解锁 " + resultRelease);
        for (int i = 0; i < 10; i++) {
            resultLock = RedisTool.tryGetDistributedLock(cluster, lockKey, uuid, 300000);
            System.out.println(i + " 锁定 " + resultLock);
            resultRelease = RedisTool.releaseDistributedLock(cluster, lockKey, uuid);
            System.out.println(i + " 解锁 " + resultRelease);
        }
        cluster.set("name","ctl");
        System.out.println(cluster.get("name"));
        cluster.close();

    }
}
