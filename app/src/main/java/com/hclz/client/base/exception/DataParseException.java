package com.hclz.client.base.exception;

/**
 * 数据解析异常封装
 */
public class DataParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param e
     */
    public DataParseException(Exception e) {
        e.printStackTrace();
    }

}
