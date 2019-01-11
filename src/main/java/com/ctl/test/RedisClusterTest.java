package com.ctl.test;

import com.ctl.utils.redis.RedisTool;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * <p>Title: RedisClusterTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-01-10 11:19
 */
public class RedisClusterTest {
    public static void main(String[] args) {
        String lockKey = "lock";
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("redis://192.168.42.29:6379")
                .addNodeAddress("redis://192.168.42.29:6380")
                .addNodeAddress("redis://192.168.42.29:6381")
                .addNodeAddress("redis://192.168.42.29:6382")
                .addNodeAddress("redis://192.168.42.29:6383")
                .addNodeAddress("redis://192.168.42.29:6384")
                .setPassword("liebe");
        RedissonClient redissonClient = Redisson.create(config);

        for (int i = 0; i < 100000000; i++) {
            try {
                String name1 = RedisTool.cluster.get("name1");
                System.out.println(name1);
            } catch (Exception e) {
                System.err.println(e);
            }
            try {
                RLock lock = redissonClient.getLock(lockKey);
                lock.lock(3, TimeUnit.SECONDS);
                System.out.println("lock");
                lock.unlock();
                System.out.println("unlock");
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
