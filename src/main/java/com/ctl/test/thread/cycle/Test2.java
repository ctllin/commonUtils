package com.ctl.test.thread.cycle;

import java.util.concurrent.TimeUnit;

/**
 * <p>Title: Test2</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-07 17:29
 */
public class Test2 {
    public static void main(String[] args) {
        final TaskLifeCycle<String> lifeCycle = new TaskLifeCycle.EmptyLifeCycle<String>() {
            public void onFinish(Thread thread, String result) {
                System.out.println("onFinsh......\tThis result is " + result);
            }
        };
        //第二个参数及为Task接口call的匿名实现
        Observable observable = new ObservableThread<>(lifeCycle, () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished done.");
            return "Hello Observer";
        });
        observable.start();
    }
}
