package com.ctl.test;

import com.ctl.utils.redis.RedisTool;

/**
 * <p>Title: RedisClusterTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-01-10 11:19
 */
public class RedisClusterTest {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {

            try {
                String name1 = RedisTool.cluster.get("name1");
                System.out.println(name1);

            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
