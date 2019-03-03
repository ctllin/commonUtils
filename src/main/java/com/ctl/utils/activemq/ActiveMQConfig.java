package com.ctl.utils.activemq;


public class ActiveMQConfig {
    public static final String USERNAME ="admin";

    public static final String PASSWORD = "admin";

    public static final String BROKEN_URL = "tcp://192.168.1.105:61616";

    public static final String TOPIC_BROKEN_URL = "failover://tcp://192.168.1.105:61616";

    public static final String QUEUE_NAME = "QUEUE_CTL_001";
    public static final String TOPIC_NAME = "TOPIC_CTL_001";

}
