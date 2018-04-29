package com.ctl.utils.concurrent;

/**@author 		guolin
 *  @date    		2016-11-17下午5:18:05
 *  @package_name
 *  @project_name   SpringTest1
 *  @version 		version.1.0
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:CountDownLatchTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015年7月30日 下午2:04:07 <br/>
 *
 * @author chiwei
 * @version
 * @since JDK 1.6
 * @see
 */
public class CyclicBarrierTest {
    static Logger logger = LoggerFactory.getLogger(CyclicBarrierTest.class);

    public static void main(String[] args) {
        ThreadPoolExecutor poolExe = new ThreadPoolExecutor(100, 1000, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(100));
        int count = 10;
        // 考试开始铃声响起，考试开始
        final CountDownLatch examBegin = new CountDownLatch(1);
        // 单个考生，考试结束交卷
        final CyclicBarrier student = new CyclicBarrier(count+1);

        // 一个考场10位考生
        for (int i = 0; i < count; i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        logger.info("考生" + Thread.currentThread().getName() + "在等待考试开始的铃声响起 examBegin.getCount()"+examBegin.getCount());
                        examBegin.await();
                        logger.info("考生听到铃声" + Thread.currentThread().getName() + "开始答题 examBegin.getCount()"+examBegin.getCount());
                        Thread.sleep((long) (Math.random() * 1000));//答题过程，真正的业务逻辑处理部分
                        logger.info("考生" + Thread.currentThread().getName() + "交卷 等待所有人交卷 student.getNumberWaiting()"+student.getNumberWaiting());
                        student.await();
                        logger.info("考生" + Thread.currentThread().getName() + "等待所有人交卷结束 student.getNumberWaiting()"+student.getNumberWaiting());
                    } catch (Exception e) {
                        logger.error("run",e);
                    }
                }
            };
            poolExe.execute(runnable); // 运动员开始任务
        }

        try {
            // 答题时间
            Thread.sleep((long) (Math.random() * 10000));
            logger.info("考场" + Thread.currentThread().getName() + "开始铃声即将响起");
            examBegin.countDown();
            logger.info("考场" + Thread.currentThread().getName() + "考试开始铃声响起");
            logger.info("########## main sleep start ##########");
            Thread.sleep( 10000);
            student.await(); // 所有考生交卷  此句子执行后10+1=0所有线程执行结束 主线程才会结束
            logger.info("########## main sleep end ##########");
            logger.info("##########考场" + Thread.currentThread().getName() + "考试结束##########");
        } catch (Exception e) {
            e.printStackTrace();
        }
        poolExe.shutdown();

    }

}