package com.hclz.client.base.bean;

import java.io.Serializable;

//@DatabaseTable(tableName = "main_account")
public class MainAccount implements Serializable {
    private static final long serialVersionUID = 6050789018036919140L;
    //	@DatabaseField(columnName = "at")
    private String at;
    //	@DatabaseField(columnName = "ct")
    private String ct;
    //	@DatabaseField(columnName = "mid")
    private String mid;
    //	@DatabaseField(columnName = "sessionid")
    private String sessionid;
    //	@DatabaseField(columnName = "ut")
    private String ut;

    private String nickname;//'nickname': '',
    private String avatar;
    //默认地址的id，由用户自己设置，可能没有
    private String default_addressid;
    //上次下单的厨房的id，用户从厨房下单时，系统自己设置，可能没有
    private String latest_cid;

    public String getHxPasswd() {
        return im_password;
    }

    public void setHxPasswd(String hxPasswd) {
        this.im_password = hxPasswd;
    }

    //环信
    private String im_password;

    public MainAccount() {
    }

    public String getAt() {
        return at;
    }


    public void setAt(String at) {
        this.at = at;
    }


    public String getCt() {
        return ct;
    }


    public void setCt(String ct) {
        this.ct = ct;
    }


    public String getMid() {
        return mid;
    }


    public void setMid(String mid) {
        this.mid = mid;
    }


    public String getSessionid() {
        return sessionid;
    }


    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }


    public String getUt() {
        return ut;
    }


    public void setUt(String ut) {
        this.ut = ut;
    }


    @Override
    public String toString() {
        return "main_account [at=" + at + ", ct=" + ct + ", mid=" + mid + ", sessionid=" + sessionid + ", ut=" + ut + "]";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDefault_addressid() {
        return default_addressid;
    }

    public void setDefault_addressid(String default_addressid) {
        this.default_addressid = default_addressid;
    }

    public String getLatest_cid() {
        return latest_cid;
    }

    public void setLatest_cid(String latest_cid) {
        this.latest_cid = latest_cid;
    }

}
