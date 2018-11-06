package com.ctl.utils.redis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>Title: TestPubSub</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-11-06 13:45
 */
public class TestPubSub {
    public static void main( String[] args )
    {
        // 连接redis服务端
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.3.117", 6379,10000,"123456");
        Publisher publisher = new Publisher(jedisPool);    //发布者
        publisher.start();

        Subscriber subscriber = new Subscriber(jedisPool);    //订阅者
        subscriber.start();


    }


}
