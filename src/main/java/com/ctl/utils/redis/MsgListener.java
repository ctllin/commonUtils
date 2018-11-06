package com.ctl.utils.redis;
import redis.clients.jedis.JedisPubSub;

/**
 * <p>Title: MsgListener</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-11-06 13:44
 */
public class MsgListener  extends JedisPubSub{

    public MsgListener(){}

    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        System.out.println(String.format("收到消息成功！ channel： %s, message： %s", channel, message));
        this.unsubscribe();
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅频道会调用
        System.out.println(String.format("订阅频道成功！ channel： %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅会调用
        System.out.println(String.format("取消订阅频道！ channel： %s, subscribedChannels： %d",channel, subscribedChannels));

    }
}
