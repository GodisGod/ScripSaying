package com.feiyu.scripsaying.callback;

/**
 * Created by HONGDA on 2016/12/29.
 */
public interface getTokenCallback {
    void finishToken(String token);
    void error(Exception e);
}
