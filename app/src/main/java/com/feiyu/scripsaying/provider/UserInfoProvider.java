package com.feiyu.scripsaying.provider;

import android.net.Uri;

import com.feiyu.scripsaying.util.HD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by HONGDA on 2016/12/15.
 */


public class UserInfoProvider implements RongIM.UserInfoProvider {
    private UserInfo userInfo = null;

    @Override
    public UserInfo getUserInfo(String userId) {
        //todo 根据ID 获取用户的头像
        BmobQuery query = new BmobQuery("User");
        query.addWhereEqualTo("user_id", userId);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    HD.TLOG("查詢用户头像成功：" + jsonArray.toString());
                    //todo 解析json获取userinfo数据
                    userInfo = anlyUserinfo(jsonArray);
                    //todo 返回UserInfo
                    /**
                     * 刷新用户缓存数据。
                     *
                     * @param userInfo 需要更新的用户缓存数据。
                     */
                    HD.TLOG("userinfo2:  " + userInfo.getName() + "  " + userInfo.getUserId() + "  " + userInfo.getPortraitUri());
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                } else {
                    HD.TLOG("失敗：" + e.getMessage() + ", " + e.getErrorCode());
                }
            }
        });
        return null;
    }

    private UserInfo anlyUserinfo(JSONArray jsonArray) {
        UserInfo userInfo = null;
        try {
            JSONObject object = jsonArray.getJSONObject(0);
            String user_name = object.getString("user_name");
            String user_id = object.getString("user_id");
            JSONObject user_icon_object = object.getJSONObject("user_icon");
            String user_icon = user_icon_object.getString("url");
            Uri uri = Uri.parse(user_icon);
            userInfo = new UserInfo(user_id, user_name, uri);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
