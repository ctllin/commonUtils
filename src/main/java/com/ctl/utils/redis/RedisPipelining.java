package com.ctl.utils.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisPipelining {

	/**
	 * @param 有时
	 *            ，我们需要采用异步方式，一次发送多个指令，不同步等待其返回结果。这样可以取得非常好的执行效率。这就是管道，调用方法如下：
	 */
	public static void main(String[] args) {
		Jedis jedis = new Jedis(RedisConfig.host,RedisConfig.port);
		jedis.auth(RedisConfig.auth);
		Pipeline pipeline = jedis.pipelined();
		long start = System.currentTimeMillis();
		pipeline.multi();
		for (int i = 0; i < 100000; i++) {
			// pipeline.set("p" + i, "p" + i);
			pipeline.del("p" + i);
		}
		pipeline.exec();
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		System.out.println("Pipelined SET: " + ((end - start) / 1000.0)
				+ " seconds results：" + results);

		jedis.disconnect();
	}

}
