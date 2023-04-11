package com.gjk.filestorage.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gjk.filestorage.enums.FileStorageType;

import lombok.Data;

/**
 * FileStorageProperties
 *
 * @author: gaojiankang
 * @date: 2023/3/9 11:50
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "custom.storage")
public class FileStorageProperties {


    /**
     * 存储类型
     */
    private FileStorageType type;

    /**
     * 存储类型对应的配置
     */
    private Config config;

    @Data
    public static class Config {

        /**
         * 自定义域名（不为空时，url用该值拼接）
         */
        private String domain;

        /**
         * 是否 https（自定义域名为空时使用该值 + bucket-name + endpoint 拼装域名）
         */
        private boolean https = false;

        /**
         * 服务端点
         */
        private String endpoint;

        /**
         * 准入 key
         */
        private String accessKey;

        /**
         * 准入密钥
         */
        private String secretKey;

        /**
         * 存储桶
         */
        private String bucketName;

        /**
         * 其它属性
         */
        private Properties properties;
    }

}
