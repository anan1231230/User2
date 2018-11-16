package com.hclz.client.base.bean;


import java.util.Map;

/**
 * 服务端返回数据基类
 *
 * @author Administrator
 */

public class BaseResponseBean {
    private String code;
    private String message;
    private String description;

    //缺少库存
    private Map<String,Integer> lacks;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String,Integer> getLacks() {
        return lacks;
    }

    public void setLacks(Map<String,Integer> lacks) {
        this.lacks = lacks;
    }
}
