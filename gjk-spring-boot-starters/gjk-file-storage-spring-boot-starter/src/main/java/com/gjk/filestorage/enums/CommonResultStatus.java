package com.gjk.filestorage.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CommonResultStatus
 *
 * @author: gaojiankang
 * @date: 2023/3/9 13:20
 * @description:
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonResultStatus {

    /**
     * 对象存储相关
     */
    OSS_ERROR("00100", "文件处理异常"),
    OSS_CONFIG_ERROR("00101", "文件存储服务配置异常"),
    OSS_UPLOAD_ERROR("00102", "文件上传失败"),
    OSS_DOWNLOAD_ERROR("00103", "文件下载失败"),
    OSS_DELETE_ERROR("00104", "文件删除失败"),
    ;

    /**
     * 返回码
     */
    private String code;

    /**
     * 提示信息（启用 i18n 时，代表 message key）
     */
    private String message;
}
