package com.ctl.utils.redis;

import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class RedisShardedJedis {
	// 分布式直连同步调用
	public static void main(String[] args) {
	//	List<JedisShardInfo> shards = Arrays.asList(new JedisShardInfo(	"192.168.42.19", 6379), new JedisShardInfo("192.168.42.19", 6380));
		JedisShardInfo jsi1=new JedisShardInfo(	RedisConfig.host, RedisConfig.port,RedisConfig.auth);
		jsi1.setPassword(RedisConfig.auth);
		List<JedisShardInfo> shards = Arrays.asList(jsi1);
		ShardedJedis sharding = new ShardedJedis(shards);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			//sharding.set("sn" + i, "n" + i);
			sharding.del("sn" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Simple@Sharing SET: " + ((end - start) / 1000.0)+ " seconds");
		sharding.disconnect();
	}
}
