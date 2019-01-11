package com.ctl.utils.redis;

import com.ctl.utils.ConfigUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: RedissonUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-01-10 16:01
 */
public class RedissonUtil {
    static final Logger logger = LoggerFactory.getLogger(RedissonUtil.class);
    private static String lockKey = "lock";
    private static Config config = new Config();
    private static RedissonClient redissonClient = getRedissonClient();

    static {
    }

    private static RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            synchronized (RedissonUtil.class) {
                if (redissonClient == null) {
                    config.useClusterServers()
                            //                .addNodeAddress("redis://192.168.42.29:6379")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node1.address") + ":" + ConfigUtils.getType("redis.cluster.node1.port"))
                            //                .addNodeAddress("redis://192.168.42.29:6380")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node2.address") + ":" + ConfigUtils.getType("redis.cluster.node2.port"))
                            //                .addNodeAddress("redis://192.168.42.29:6381")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node3.address") + ":" + ConfigUtils.getType("redis.cluster.node3.port"))
                            //                .addNodeAddress("redis://192.168.42.29:6382")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node4.address") + ":" + ConfigUtils.getType("redis.cluster.node4.port"))
                            //                .addNodeAddress("redis://192.168.42.29:6383")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node5.address") + ":" + ConfigUtils.getType("redis.cluster.node5.port"))
                            //                .addNodeAddress("redis://192.168.42.29:6384")
                            .addNodeAddress("redis://" + ConfigUtils.getType("redis.cluster.node6.address") + ":" + ConfigUtils.getType("redis.cluster.node6.port"))
                            .setPassword(ConfigUtils.getType("redis.cluster.password"));
                    try {
                        logger.debug("clusterServersConfig={},config={}", config.isClusterConfig(), config.toJSON());
                    } catch (IOException e) {
                        logger.debug("clusterServersConfig={}", config.isClusterConfig());
                    }
                    redissonClient = Redisson.create(config);
                    return redissonClient;
                } else {
                    return redissonClient;
                }
            }
        } else {
            return redissonClient;
        }
    }


    public static void main(String[] args) {
        String lock = "redisson_lock";
        for (int i = 0; i < 1000; i++) {
           try{
               RLock rLock = getRedissonClient().getLock(lock);
               RedisThreadRedissonLock tLock = new RedisThreadRedissonLock(rLock);
               Thread lockThread = new Thread(tLock);
               //lockThread.setDaemon(true);
               lockThread.start();
           }catch (Exception e){
               System.err.println(e);
           }

        }
    }

    static class RedisThreadRedissonLock implements Runnable {
        private RLock rLock;

        public RedisThreadRedissonLock() {
        }

        public RedisThreadRedissonLock(RLock rLock) {
            this.rLock = rLock;
        }

        public void run() {
            try {
                rLock.lock(new Random().nextInt(1000), TimeUnit.MILLISECONDS);
                logger.info("lock锁定");
                Thread.sleep(new Random().nextInt(1000));
                rLock.unlock();
                logger.info("unlock解锁");
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
