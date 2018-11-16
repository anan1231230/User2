package com.hclz.client.base.constant;

/**
 * 定义信息code以及信息内容
 */
public enum MessageConstant {

    // 以下定义服务器端数据返回数据
    // 请求数据成功但是数据为空
    GET_DATA_SUCCESSFUL("0000", "请求数据为空")
    // 请求数据成功，且数据不为空
    , GET_DATA_SUCCESSFUL_NO_DATA("0001", "请求数据成功")


    // 以下定义客户端错误号
    // 服务器请求时发生网络错误
    // 服务器请求时发生IO错误
    , IO_EXCEPTION("1001", "数据请求异常,请检查网络连接是否良好")
    // 服务器请求时发生网络错误
    , NETWORK_DISCONNECT_EXCEPTION("1002", "网络连接不上，请检查网络连接")
    // 数据解析错误
    , DATAPARSE_EXCEPTION("2001", "数据解析异常");

    // code数值
    private String msgcode;
    // 信息内容
    private String msgcontent;

    // 定义信息
    private MessageConstant(String msgcode, String msgcontent) {
        this.msgcode = msgcode;
        this.msgcontent = msgcontent;
    }

    public String getMsgcode() {
        return msgcode;
    }

    public String getMsgcontent() {
        return msgcontent;
    }
}
