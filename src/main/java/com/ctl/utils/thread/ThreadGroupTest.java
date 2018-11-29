package com.ctl.utils.thread;

import java.util.concurrent.TimeUnit;

/**
 * <p>Title: ThreadGroupTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-11-29 20:28
 */
public class ThreadGroupTest {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t,e)->{
            System.out.println(t.getName()+"\t"+t.getId());
//            e.printStackTrace();
            System.out.println(e.getMessage());
        });
        ThreadGroup threadGroup1 = new ThreadGroup("threadGroup1");
        new Thread(threadGroup1,()->{
            while(true){
                System.out.println("threadGroup1-" +"thread1");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(1/0);
            }
        },"thread1").start();
        new Thread(threadGroup1,()->{
            while(true){
                System.out.println("threadGroup1-" +"thread2");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(1/0);
            }
        },"thread2").start();
        threadGroup1.setDaemon(true);
        ThreadGroup threadGroupMain = Thread.currentThread().getThreadGroup();
        System.out.println(threadGroupMain.getName());
        System.out.println(threadGroupMain.getParent());
        System.out.println(threadGroupMain.getParent().getParent());
    }
}
