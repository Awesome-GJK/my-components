package com.gjk.filestorage.exception;

import com.gjk.filestorage.enums.CommonResultStatus;

/**
 * FileStorageException
 *
 * @author: gaojiankang
 * @date: 2023/3/9 13:22
 * @description:
 */
public class FileStorageException extends RuntimeException {

    /**
     * 异常code
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    public FileStorageException(Throwable throwable, String code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public FileStorageException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public FileStorageException(Throwable throwable, CommonResultStatus resultStatus) {
        super(throwable);
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }

    public FileStorageException(CommonResultStatus resultStatus) {
        super();
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }
}
