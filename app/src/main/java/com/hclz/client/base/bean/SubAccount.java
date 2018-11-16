package com.hclz.client.base.bean;

public class SubAccount {
    private String mid;
    private String sid;
    private String type;

    public SubAccount() {
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "sub_account [mid=" + mid + ", sid=" + sid + ", type=" + type + "]";
    }

}
