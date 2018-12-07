package com.ctl.test.thread.cycle;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: Test</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-07 17:17
 */
public class Test {
    public static void main(String[] args) {
        Observable observableThread = new ObservableThread<>(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished done.");
            return null;
        });
        observableThread.start();

        final TaskLifeCycle<String> lifeCycle = new TaskLifeCycle.EmptyLifeCycle<String>() {
            public void onFinish(Thread thread, String result) {
                System.out.println("This result is " + result);
            }
        };
        Observable observable = new ObservableThread<>(lifeCycle, () -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finished done.");
            return "Hello Observer";
        });
        observable.start();
    }
}
