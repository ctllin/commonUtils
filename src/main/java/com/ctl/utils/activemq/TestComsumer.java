package com.ctl.utils.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: TestComsumer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2019-03-02 18:42
 */
public class TestComsumer {
    static Logger logger = LoggerFactory.getLogger(TestComsumer.class);

    public static void main(String[] args){
        Comsumer comsumer = new Comsumer();
        comsumer.init();
        TestComsumer testConsumer = new TestComsumer();
        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
//        new Thread(testConsumer.new ConsumerMq(comsumer)).start();
    }

    private class ConsumerMq implements Runnable{
        Comsumer comsumer;
        public ConsumerMq(Comsumer comsumer){
            this.comsumer = comsumer;
        }

        @Override
        public void run() {
            while(true){
                try {
                    comsumer.getMessage(ActiveMQConfig.QUEUE_NAME);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}