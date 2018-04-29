package com.ctl.utils.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisSetJava {
	public static void main(String[] args) {
		// 连接本地的 Redis 服务
		Jedis jedis = new Jedis(RedisConfig.host,RedisConfig.port);
		jedis.auth(RedisConfig.auth);
		System.out.println("Connection to server sucessfully");
		// 存储数据到列表中
		Map<String,String> mapSet=new HashMap<String,String>();
		mapSet.put("ctl", "你");
		mapSet.put("lin", "还好吗");
		jedis.hmset("tutorial-map", mapSet);
		
		List<String> list=jedis.hmget("tutorial-map", "ctl","lin");
		System.err.println(list);
		// 获取存储的数据并输出
		jedis.disconnect();
	}
}