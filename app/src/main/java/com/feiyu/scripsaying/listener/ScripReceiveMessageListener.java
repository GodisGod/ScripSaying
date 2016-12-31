package com.feiyu.scripsaying.listener;


import com.feiyu.scripsaying.util.HD;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by HONGDA on 2016/12/16.
 */
public class ScripReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        //开发者根据自己需求自行处理
        HD.TLOG("boolean onReceived1: " + message.getTargetId() + "  " + message.getSenderUserId() + " " + message.getContent().toString());

        return false;
    }
}
