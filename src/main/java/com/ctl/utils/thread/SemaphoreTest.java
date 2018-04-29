package com.ctl.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**@author 		guolin
 *  @date    		2016-5-17下午5:14:02
 *  @package_name bean
 *  @project_name   Download
 *  @version 		version.1.0
 */
public class SemaphoreTest {

    public static void main(String[] args) {  
       // 线程池
       ExecutorService exec = Executors.newCachedThreadPool();  
       // 只能5个线程同时访
       final Semaphore semp = new Semaphore(5);  
       // 模拟20个客户端访问 
       for (int index = 0; index < 20; index++) {
           final int NO = index;  
           Runnable run = new Runnable() {  
               public void run() {  
                   try {  
                       // 获取许可 
                       semp.acquire();  
                       System.out.println("Accessing: " + NO);  
                       Thread.sleep((long) (Math.random() * 10000));  
                       // 访问完后，释锁如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程则进入阻塞
                       semp.release();  
                   } catch (InterruptedException e) {  
                   }  
               }  
           };  
           exec.execute(run);  
       }  
       // 关闭线程池
       exec.shutdown();  
   }  
}

