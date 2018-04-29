package com.ctl.utils.redis;

import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

public class RedisShardedJedisPipelinePool {
	// 分布式连接池异步调用
	public static void main(String[] args) {

		JedisShardInfo jsi1=new JedisShardInfo(	RedisConfig.host, RedisConfig.port,RedisConfig.auth);
		jsi1.setPassword(RedisConfig.auth);
		List<JedisShardInfo> shards = Arrays.asList(jsi1);
		ShardedJedisPool pool = new ShardedJedisPool(new JedisPoolConfig(),shards);
		ShardedJedis one = pool.getResource();
		ShardedJedisPipeline pipeline = one.pipelined();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			pipeline.set("sppn" + i, "n" + i);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		long end = System.currentTimeMillis();
		pool.returnResource(one);
		System.out.println("Pipelined@Pool SET: " + ((end - start) / 1000.0) + " seconds results.size()="+results.size());
		pool.destroy();
	}
}
