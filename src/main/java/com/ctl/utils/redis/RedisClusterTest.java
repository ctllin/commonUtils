package com.ctl.utils.redis;

import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * <p>Title: RedisClusterTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-05-08 14:46
 */
public class RedisClusterTest {
    public static void main(String[] args) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(1);
        // 最大空闲数
        poolConfig.setMaxIdle(1);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<>();
        nodes.add(new HostAndPort("192.168.42.3", 6379));
        nodes.add(new HostAndPort("192.168.42.3", 6380));
        nodes.add(new HostAndPort("192.168.42.3", 6381));
        nodes.add(new HostAndPort("192.168.42.3", 6382));
        nodes.add(new HostAndPort("192.168.42.3", 6383));
        nodes.add(new HostAndPort("192.168.42.3", 6384));
        JedisCluster cluster = new JedisCluster(nodes, poolConfig);
        cluster.set("uuid", UUID.randomUUID().toString());
        String uuid = cluster.get("uuid");
        System.out.println(uuid);
        cluster.set("age", "18");
        System.out.println(cluster.get("age"));
        try {
            cluster.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
