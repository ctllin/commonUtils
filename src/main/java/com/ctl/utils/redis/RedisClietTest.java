package com.ctl.utils.redis;

import redis.clients.jedis.Jedis;

public class RedisClietTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 连接本地的 Redis 服务
		Jedis jedis = new Jedis(RedisConfig.host,RedisConfig.port);
		jedis.auth(RedisConfig.auth);
		System.out.println("Connection to server sucessfully");
		// 查看服务是否运行
		System.out.println("Server is running: " + jedis.ping());
		 jedis.set("w3ckey", "Redis tutorial");
	     // 获取存储的数据并输出
	     System.out.println("Stored string in redis:: "+ jedis.get("w3ckey"));
	     jedis.disconnect();
	}

}
