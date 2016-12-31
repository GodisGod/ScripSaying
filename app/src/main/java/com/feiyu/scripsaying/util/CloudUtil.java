package com.feiyu.scripsaying.util;

import com.feiyu.scripsaying.callback.getTokenCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

/**
 * Created by HONGDA on 2016/12/29.
 */
public class CloudUtil {
    private getTokenCallback mgetTokenCallback;

    public CloudUtil(getTokenCallback getTokenCallback) {
        this.mgetTokenCallback = getTokenCallback;
    }

    public  void getTokenFromCloud(String userId, String userName) {
        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
//第一个参数是上下文对象，第二个参数是云端逻辑的方法名称，第三个参数是上传到云端逻辑的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
        JSONObject cloudCodeParams = new JSONObject();
        try {
            cloudCodeParams.put("userId", userId);
            cloudCodeParams.put("name", userName);
            cloudCodeParams.put("portraitUri", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ace.callEndpoint("getToken", cloudCodeParams, new CloudCodeListener() {
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    String result1 = object.toString().replace("\\", "");
                    String result = result1.substring(1, result1.length() - 1);
                    HD.LOG("云端逻辑返回值：" + result);
                    //解析token
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String token = null;
                        if (jsonObject.getString("token")!=null){
                             token = jsonObject.getString("token");
                        }else {
                            HD.TLOG("数据异常");
                        }
                        if (mgetTokenCallback!=null){
                            mgetTokenCallback.finishToken(token);
                        }
                    } catch (JSONException error) {
                        error.printStackTrace();
                        if (mgetTokenCallback!=null){
                            mgetTokenCallback.error(error);
                        }
                    }
                } else {
                    HD.LOG(" " + e.getMessage());
                }
            }
        });
    }


}
