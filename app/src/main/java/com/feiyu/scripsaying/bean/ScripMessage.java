package com.feiyu.scripsaying.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by HONGDA on 2016/12/31.
 */
public class ScripMessage extends BmobObject {
    private String userId;          //发送者ID
    private String userType;       //发送者类型萝莉御姐少年绅士等
    private String userGender;     //发送者性别
    private int scripType;          //纸片类型的图片描述 足球篮球运动健身
    private String scripTypeText;          //纸片类型的文字描述 足球篮球运动健身
    private BmobFile scripImg;         //纸片图片
    private BmobFile scripAudio;      //纸片语音
    private String scripText;   //纸片内容
    private String level;            //纸片等级
    private BmobGeoPoint bmobGeoPoint;//纸片经纬度


    public ScripMessage() {
    }

    public String getScripText() {
        return scripText;
    }

    public void setScripText(String scripText) {
        this.scripText = scripText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public int getScripType() {
        return scripType;
    }

    public void setScripType(int scripType) {
        this.scripType = scripType;
    }

    public String getScripTypeText() {
        return scripTypeText;
    }

    public void setScripTypeText(String scripTypeText) {
        this.scripTypeText = scripTypeText;
    }


    public BmobFile getScripImg() {
        return scripImg;
    }

    public void setScripImg(BmobFile scripImg) {
        this.scripImg = scripImg;
    }

    public BmobFile getScripAudio() {
        return scripAudio;
    }

    public void setScripAudio(BmobFile scripAudio) {
        this.scripAudio = scripAudio;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BmobGeoPoint getBmobGeoPoint() {
        return bmobGeoPoint;
    }

    public void setBmobGeoPoint(BmobGeoPoint bmobGeoPoint) {
        this.bmobGeoPoint = bmobGeoPoint;
    }
}
