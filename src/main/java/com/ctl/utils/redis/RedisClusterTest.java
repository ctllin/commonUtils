package com.ctl.utils.redis;

import com.ctl.utils.DateUtil;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.Date;
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

//        Config config = new Config();
//        config.useClusterServers()
//                .setScanInterval(5000) // cluster state scan interval in milliseconds
//                .addNodeAddress("192.168.42.3:6379")
//                .addNodeAddress("192.168.42.3:6380")
//                .addNodeAddress("192.168.42.3:6381")
//                .addNodeAddress("192.168.42.3:6382")
//                .addNodeAddress("192.168.42.3:6383")
//                .addNodeAddress("192.168.42.3:6384");
//        RedissonClient redisson = Redisson.create(config);
//        System.out.println(redisson.getKeys());


        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(60000);
        // 最大空闲数
        poolConfig.setMaxIdle(1000);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(5000);

        poolConfig.setTestOnBorrow(true);


        Set<HostAndPort> nodes = new LinkedHashSet<>();
        //加入前面三个为master后面三个为从属当关闭前三个后 后三个会自动变master 然后从后三个获取数据
        nodes.add(new HostAndPort("192.168.42.3", 6379));//master
        nodes.add(new HostAndPort("192.168.42.3", 6380));//master
        nodes.add(new HostAndPort("192.168.42.3", 6381));//master
        nodes.add(new HostAndPort("192.168.42.3", 6382));
        nodes.add(new HostAndPort("192.168.42.3", 6383));
        nodes.add(new HostAndPort("192.168.42.3", 6384));

//        JedisPool pool = new JedisPool(poolConfig, "192.168.42.3",6379, 100000);
//        System.out.println(pool.getResource().get("uuid"));
        JedisCluster cluster = new JedisCluster(nodes, poolConfig);
        //cluster.set("uuid", UUID.randomUUID().toString());
        String uuid = cluster.get("uuid");
        System.out.println(uuid);
      //  cluster.set("age", "18");
        System.out.println(cluster.get("age"));

        for(int i=0;i<100;i++){
             //cluster.set("a"+i, i+"    "+DateUtil.sdfyyyy_MM_dd_HH_mm_ss.format(new Date()));
        }
        for(int i=0;i<100;i++){
            System.out.println(cluster.get("a"+i));
        }
        try {
            cluster.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
