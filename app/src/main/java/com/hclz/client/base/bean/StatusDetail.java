package com.hclz.client.base.bean;

public class StatusDetail {

    //	{"desc":"提交订单成功","operator":"7","timestamp":"2015-11-03 14:32:54"}
    private String desc;
    private String operator;
    private String timestamp;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
