package com.ctl.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>Title: TaskForTest</p>
 * <p>Description:task test class </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.ctl.com</p>
 *
 * @author ctl
 * @version 1.0
 * @date 2018-04-26 10:43
 */
@Component
public class TaskForTest {
    public static Logger logger = LoggerFactory.getLogger(TaskForTest.class);
    public void  doTask(){
        logger.info("TaskForTest...............");
    }
}
