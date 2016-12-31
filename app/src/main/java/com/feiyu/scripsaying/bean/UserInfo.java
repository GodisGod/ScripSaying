package com.feiyu.scripsaying.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by YueDong on 2016/12/27.
 */
public class UserInfo extends BmobObject {
    private String userID;      //用户ID 在服务端设置为唯一 使用ObjectId
    private String userName;       //名字
    private String userIcon;//头像
    private String userGender;//性别
    private String userPassword;//密码
    private String City;//城市
    private String Sign;//签名
    private String Level;//VIP等级
    private String Token;//会话Token
    private String regPlatform;//注册平台（QQ 微信 微博 APP注册）
    private String Type;//用户类型（萝莉 御姐  成熟女士）

    public UserInfo() {
    }

    public UserInfo(String userID, String userName, String userIcon, String userGender, String userPassword, String city, String sign, String level, String token, String regPlatform, String type) {
        this.userID = userID;
        this.userName = userName;
        this.userIcon = userIcon;
        this.userGender = userGender;
        this.userPassword = userPassword;
        City = city;
        Sign = sign;
        Level = level;
        Token = token;
        this.regPlatform = regPlatform;
        Type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRegPlatform() {
        return regPlatform;
    }

    public void setRegPlatform(String regPlatform) {
        this.regPlatform = regPlatform;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
