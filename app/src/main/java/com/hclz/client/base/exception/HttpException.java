package com.hclz.client.base.exception;

/**
 * http请求异常封装
 */
public class HttpException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param e
     */
    public HttpException(Exception e) {
        e.printStackTrace();
    }

}
