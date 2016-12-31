package com.feiyu.scripsaying.listener;


import com.feiyu.scripsaying.util.HD;

import io.rong.imlib.RongIMClient;

/**
 * Created by HONGDA on 2016/12/16.
 */
public class ScripConnectStatusListener implements RongIMClient.ConnectionStatusListener {
    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus) {

            case CONNECTED://连接成功。
                HD.LOG("连接成功");
                break;
            case DISCONNECTED://断开连接。
                HD.LOG("断开连接");
                break;
            case CONNECTING://连接中。
                HD.LOG("连接中");
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                HD.LOG("网络不可用");
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                HD.LOG("用户账户在其他设备登录，本机会被踢掉线");
                break;
        }
    }
}
