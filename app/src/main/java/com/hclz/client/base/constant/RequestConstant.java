package com.hclz.client.base.constant;

/**
 * 定义发送信息内容
 */
public enum RequestConstant {
    PUT_CONTENT("content", "json字符串，即每个接口实际请求内容"),
    PUT_SIGNATURE("signature", "对content字符串使用与服务器通过sig_kv约定的key做的签名"),
    PUT_SIG_KV("sig_kv", "签名使用的key的版本 signature key version"),
    PUT_TIMESTAMP("timestamp", "utc毫秒数"),;

    // code数值
    private String msg;
    // 信息内容
    private String msgcontent;

    // 定义信息
    private RequestConstant(String msg, String msgcontent) {
        this.msg = msg;
        this.msgcontent = msgcontent;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgcontent() {
        return msgcontent;
    }
}
