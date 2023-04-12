package com.gjk.http.exception;

/**
 * HttpException
 *
 * @author: gaojiankang
 * @date: 2023/4/11 19:34
 * @description:
 */
public class HttpException extends RuntimeException {

    /**
     * 异常code
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    public HttpException(String code, String message, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public HttpException(String code, String message) {
        this(code, message, null);
    }

    public HttpException(ResultStatus status, Throwable throwable) {
        super(throwable);
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public HttpException(ResultStatus status) {
        this(status, null);
    }
}
