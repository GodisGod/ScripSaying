package com.feiyu.scripsaying.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by HONGDA on 2016/12/31.
 */
public class ScripMessage extends BmobObject {
    private String sendUserId;          //发送者ID
    private String sendUserType;       //发送者类型萝莉御姐少年绅士等
    private String sendUserGender;     //发送者性别
    private BmobFile ScripType;          //纸片类型的图片描述 足球篮球运动健身
    private String ScripTypeText;          //纸片类型的文字描述 足球篮球运动健身
    private BmobFile ScripImg;         //纸片图片
    private BmobFile ScripAudio;      //纸片语音
    private String Scriptext;   //纸片内容
    private String level;            //纸片等级
    private BmobGeoPoint bmobGeoPoint;//纸片经纬度


    public ScripMessage() {
    }

    public String getScriptext() {
        return Scriptext;
    }

    public void setScriptext(String scriptext) {
        Scriptext = scriptext;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserType() {
        return sendUserType;
    }

    public void setSendUserType(String sendUserType) {
        this.sendUserType = sendUserType;
    }

    public String getSendUserGender() {
        return sendUserGender;
    }

    public void setSendUserGender(String sendUserGender) {
        this.sendUserGender = sendUserGender;
    }

    public BmobFile getScripType() {
        return ScripType;
    }

    public void setScripType(BmobFile scripType) {
        ScripType = scripType;
    }

    public String getScripTypeText() {
        return ScripTypeText;
    }

    public void setScripTypeText(String scripTypeText) {
        ScripTypeText = scripTypeText;
    }


    public BmobFile getScripImg() {
        return ScripImg;
    }

    public void setScripImg(BmobFile scripImg) {
        ScripImg = scripImg;
    }

    public BmobFile getScripAudio() {
        return ScripAudio;
    }

    public void setScripAudio(BmobFile scripAudio) {
        ScripAudio = scripAudio;
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
