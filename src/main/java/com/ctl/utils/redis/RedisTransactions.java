package com.ctl.utils.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisTransactions {

	/**
	 * @param redis的事务很简单，他主要目的是保障，一个client发起的事务中的命令可以连续的执行，而中间不会插入其他client的命令。
	 * 我们调用jedis.watch(…)方法来监控key，如果调用后key值发生变化，则整个事务会执行失败。
	 * 另外，事务中某个操作失败，并不会回滚其他操作。这一点需要注意。还有，我们可以使用discard()方法来取消事务
	 */
	public static void main(String[] args) {
		Jedis jedis = new Jedis(RedisConfig.host,RedisConfig.port);
		jedis.auth(RedisConfig.auth);
	    long start = System.currentTimeMillis();
	    Transaction tx = jedis.multi();
	    for (int i = 0; i < 1000; i++) {
	        //tx.set("t" + i, "t" + i);
	        tx.del("t" + i);
	        tx.del("tx"+i,"sppn"+i);
	    }
	    List<Object> results = tx.exec();
	    long end = System.currentTimeMillis();
	    System.out.println("Transaction SET: " + ((end - start)/1000.0) + " seconds  results:"+results);
	    jedis.disconnect();
	}

}
