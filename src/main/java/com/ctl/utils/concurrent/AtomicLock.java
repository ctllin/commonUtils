package com.ctl.utils.concurrent;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>Title: AtomicLock</p>
 * <p>Description: 自定义锁 </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-03-09 13:40
 */
public class AtomicLock implements Lock {
    //针对线程的原子操作了
    AtomicReference<Thread> threadResource = new AtomicReference<>();
    public Queue<Thread> waiter = new LinkedBlockingQueue<>();

    public void lock() {
        while (!threadResource.compareAndSet(null, Thread.currentThread())) {
            waiter.add(Thread.currentThread());
            System.out.println("开始等待......");
            LockSupport.park();
            System.out.println("等待结束......");
            waiter.remove(Thread.currentThread());
        }
    }

    public void unlock() {
        if (threadResource.compareAndSet(Thread.currentThread(), null)) {
            Thread[] threads = waiter.toArray(new Thread[]{});
            for (int i = 0; i < threads.length; i++) {
                Thread thread = threads[i];
                LockSupport.unpark(thread);
                System.out.println("唤醒所有......");
            }
        }
    }

    public void lockInterruptibly() throws InterruptedException {

    }


    public boolean tryLock() {
        return false;
    }


    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    public Condition newCondition() {
        return null;
    }


}

class AtomicLockTest {
    AtomicLock lock = new AtomicLock();
    int i = 0;
    int j = 0;
    int m = 0;
    int n = 0;

    public void incr() {
        i++;
        j++;
    }

    public void incrLock() {
        lock.lock();
        m++;
        n++;
        lock.unlock();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        AtomicLockTest atomicLockTest = new AtomicLockTest();
        for (int k = 0; k < 6; k++) {
            new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    atomicLockTest.incr();
                    atomicLockTest.incrLock();
                }
            }).start();
        }
        long end = System.currentTimeMillis();
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {

        }
        System.out.println(atomicLockTest.i + "\t" + atomicLockTest.j);//59952	59955
        System.out.println(atomicLockTest.m + "\t" + atomicLockTest.n);//60000	60000
    }

}