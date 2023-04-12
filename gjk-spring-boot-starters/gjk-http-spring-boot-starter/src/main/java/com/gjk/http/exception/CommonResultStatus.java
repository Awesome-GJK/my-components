package com.gjk.http.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CommonResultStatus
 *
 * @author: gaojiankang
 * @date: 2023/4/11 19:38
 * @description:
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonResultStatus implements ResultStatus {
    REQUEST_ERROR("000", "请求异常"),
    REQUEST_TIMEOUT("001", "请求超时"),
    ;

    /**
     * 返回码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;
}
