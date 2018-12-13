package com.ctl.test;

import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * <p>Title: StampedLockTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-13 15:04
 */

public class StampedLockTest {
    private double x, y;//内部定义表示坐标点
    private final StampedLock sl = new StampedLock();//定义了StampedLock锁,

    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();//这里的含义和distanceFormOrigin方法中 sl.readLock()是类似的
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);//退出临界区,释放写锁
        }
    }

    double distanceFromOrigin() {//只读方法
        long stamp = sl.tryOptimisticRead();  //试图尝试一次乐观读 返回一个类似于时间戳的邮戳整数stamp 这个stamp就可以作为这一个所获取的凭证
        double currentX = x, currentY = y;//读取x和y的值,这时候我们并不确定x和y是否是一致的
        if (!sl.validate(stamp)) {//判断这个stamp是否在读过程发生期间被修改过,如果stamp没有被修改过,责任无这次读取时有效的,因此就可以直接return了,反之,如果stamp是不可用的,则意味着在读取的过程中,可能被其他线程改写了数据,因此,有可能出现脏读,如果如果出现这种情况,我们可以像CAS操作那样在一个死循环中一直使用乐观锁,知道成功为止
            stamp = sl.readLock();//也可以升级锁的级别,这里我们升级乐观锁的级别,将乐观锁变为悲观锁, 如果当前对象正在被修改,则读锁的申请可能导致线程挂起.
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);//退出临界区,释放读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    System.out.println("moveIfAtOrigin---x=" + x + ",y=" + y);
                    break;
                } else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();//获取到writeLock后 再次使用tryConvertToWriteLock 得到的stamp是同一个
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }

    public static void main(String[] args) {

//        IntStream.range(1,10000).boxed().forEach(i->new Thread(()->{
//            StampedLockTest test = new StampedLockTest();
//            test.move(Math.random(),Math.random());
//        }).start());
//        IntStream.range(1,10000).boxed().forEach(i->new Thread(()->{
//            StampedLockTest test = new StampedLockTest();
//            test.moveIfAtOrigin(Math.random(),Math.random());
//        }).start());
//        IntStream.range(1,10000).boxed().forEach(i->new Thread(()->{
//            StampedLockTest test = new StampedLockTest();
//            test.distanceFromOrigin();
//        }).start());
        StampedLock sl = new StampedLock();//定义了StampedLock锁,
       long stamp = sl.writeLock();
        System.out.println(stamp);
        stamp = sl.tryConvertToWriteLock(stamp);
        System.out.println(stamp);
    }
}
