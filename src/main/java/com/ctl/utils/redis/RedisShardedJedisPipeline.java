package com.ctl.utils.redis;

import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

public class RedisShardedJedisPipeline {
	// 分布式直连异步调用
	public static void main(String[] args) {

		JedisShardInfo jsi1=new JedisShardInfo(	RedisConfig.host, RedisConfig.port,RedisConfig.auth);
		jsi1.setPassword(RedisConfig.auth);
		List<JedisShardInfo> shards = Arrays.asList(jsi1);
		ShardedJedis sharding = new ShardedJedis(shards);
	    ShardedJedisPipeline pipeline = sharding.pipelined();
	    long start = System.currentTimeMillis();
	    for (int i = 0; i < 10; i++) {
	        pipeline.set("sp" + i, "p" + i);
	    }
	    List<Object> results = pipeline.syncAndReturnAll();
	    long end = System.currentTimeMillis();
	    System.out.println("Pipelined@Sharing SET: " + ((end - start)/1000.0) + " seconds results:"+results);
	    sharding.disconnect();
	}
}
