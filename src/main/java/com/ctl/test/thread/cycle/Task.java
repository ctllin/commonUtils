package com.ctl.test.thread.cycle;

/**
 * <p>Title: Task</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-07 17:04
 */
public interface Task<T> {
    T call();
}
