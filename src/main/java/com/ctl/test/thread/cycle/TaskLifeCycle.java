package com.ctl.test.thread.cycle;

import net.sf.json.JSONObject;

/**
 * <p>Title: TaskLifeCycle</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-07 17:02
 */
public interface TaskLifeCycle<T> {
    void onStart(Thread thread);
    void onRunning(Thread thread);
    void onFinsh(Thread thread,T result);
    void onError(Thread thread,Exception e);
    class EmptyLifeCycle<T> implements TaskLifeCycle<T>{
        @Override
        public void onStart(Thread thread) {
            System.out.println("onStart......");
        }

        @Override
        public void onRunning(Thread thread) {
            System.out.println("onRunning......");
        }

        @Override
        public void onFinsh(Thread thread, T result) {
            System.out.println("onFinsh......" + JSONObject.fromObject(result));
        }

        @Override
        public void onError(Thread thread, Exception e) {
            System.out.println("onError......");
        }
    }
}
