package com.feiyu.scripsaying.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by YueDong on 2016/12/27.
 */
public class UserInfo extends BmobObject {
    private String userId;      //用户ID 在服务端设置为唯一 使用ObjectId
    private String userName;       //名字
    private String userIcon;//头像
    private String userGender;//性别
    private String userType;//类型
    private String userPassword;//密码
    private String city;//城市
    private String sign;//签名
    private String level;//VIP等级
    private String token;//会话Token
    private String regPlatform;//注册平台（QQ 微信 微博 APP注册）
    private String type;//用户类型（萝莉 御姐  成熟女士）

    public UserInfo() {
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserInfo(String userID, String userName, String userIcon, String userGender, String userPassword, String city, String sign, String level, String token, String regPlatform, String type) {
        this.userId = userID;
        this.userName = userName;
        this.userIcon = userIcon;
        this.userGender = userGender;
        this.userPassword = userPassword;
        this.city = city;
        this.sign = sign;
        this.level = level;
        this.token = token;
        this.regPlatform = regPlatform;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegPlatform() {
        return regPlatform;
    }

    public void setRegPlatform(String regPlatform) {
        this.regPlatform = regPlatform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
