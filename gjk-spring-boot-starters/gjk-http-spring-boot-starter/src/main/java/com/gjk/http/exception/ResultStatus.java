package com.gjk.http.exception;

/**
 * ResultStatus
 *
 * @author: gaojiankang
 * @date: 2023/4/11 19:37
 * @description:
 */
public interface ResultStatus {
    /**
     * 获取返回码
     *
     * @return
     */
    String getCode();

    /**
     * 获取提示信息
     *
     * @return
     */
    String getMessage();
}
