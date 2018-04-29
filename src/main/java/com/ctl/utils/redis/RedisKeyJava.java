package com.ctl.utils.redis;

//Redis Java Keys 实例

import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisKeyJava {
	public static void main(String[] args) {
		// 连接本地的 Redis 服务
		Jedis jedis = new Jedis(RedisConfig.host,RedisConfig.port);
		jedis.auth(RedisConfig.auth);
		System.out.println("Connection to server sucessfully");
		// 获取数据并输出
		Set<String> set = jedis.keys("*");
		System.out.println(set.size()+""+set);
		jedis.disconnect();
	}
}