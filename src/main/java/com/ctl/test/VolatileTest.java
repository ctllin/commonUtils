package com.ctl.test;

import java.util.concurrent.TimeUnit;

/**
 * <p>Title: Volatile</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-03 11:46
 */
public class VolatileTest {
    final static int MAX = 5;
    //static volatile int init_value = 0;//线程会执行结束
    static  int init_value = 0;//线程无法推出

    public static void main(String[] args) {
        new Thread(() -> {
            int localValue = init_value;
            while (localValue < MAX) {
                if (init_value != localValue) {
                    System.out.printf("The init_value is updated to[%d]\n", init_value);
                    localValue = init_value;
                }
            }
        }, "reader").start();
        new Thread(() -> {
            int localValue = init_value;
            while (localValue < MAX) {

                System.out.printf("The init_value will be change to[%d]\n", ++localValue);
                init_value = localValue;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "updater").start();
    }
}
