package com.feiyu.scripsaying.bean;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by HONGDA on 2017/1/1.
 */
public class DiscoverScrip {
    //根据user表查询
    private String sendUserName;          //发送者姓名
    private String sendUserIcon;          //发送者头像
    private String userType;              //发送者类型
    //根据Scrip表查询
    private String sendUserId;          //发送者ID
    private String sendUserGender;     //发送者性别
    private BmobFile scripType;          //纸片类型的图片描述 足球篮球运动健身
    private String scripTypeText;          //纸片类型的文字描述 足球篮球运动健身
    private BmobFile scripImg;         //纸片图片
    private BmobFile scripAudio;      //纸片语音
    private String scriptext;   //纸片内容
    private String level;            //纸片等级
    private BmobGeoPoint bmobGeoPoint;//纸片经纬度

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserIcon() {
        return sendUserIcon;
    }

    public void setSendUserIcon(String sendUserIcon) {
        this.sendUserIcon = sendUserIcon;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserGender() {
        return sendUserGender;
    }

    public void setSendUserGender(String sendUserGender) {
        this.sendUserGender = sendUserGender;
    }

    public BmobFile getScripType() {
        return scripType;
    }

    public void setScripType(BmobFile scripType) {
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

    public String getScriptext() {
        return scriptext;
    }

    public void setScriptext(String scriptext) {
        this.scriptext = scriptext;
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

    @Override
    public String toString() {
        return "DiscoverScrip{" +
                "sendUserName='" + sendUserName + '\'' +
                ", sendUserIcon='" + sendUserIcon + '\'' +
                ", userType='" + userType + '\'' +
                ", sendUserId='" + sendUserId + '\'' +
                ", sendUserGender='" + sendUserGender + '\'' +
                ", scripType=" + scripType +
                ", scripTypeText='" + scripTypeText + '\'' +
                ", scripImg=" + scripImg +
                ", scripAudio=" + scripAudio +
                ", scriptext='" + scriptext + '\'' +
                ", level='" + level + '\'' +
                ", bmobGeoPoint=" + bmobGeoPoint +
                '}';
    }
}
