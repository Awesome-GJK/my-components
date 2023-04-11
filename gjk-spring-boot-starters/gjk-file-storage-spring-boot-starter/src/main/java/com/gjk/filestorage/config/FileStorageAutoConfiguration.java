package com.gjk.filestorage.config;

import java.util.Optional;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.gjk.filestorage.core.FileStorage;
import com.gjk.filestorage.core.storage.AliOssStorage;
import com.gjk.filestorage.core.storage.HaWeiObsStorage;
import com.gjk.filestorage.enums.CommonResultStatus;
import com.gjk.filestorage.enums.FileStorageType;
import com.gjk.filestorage.exception.FileStorageException;


/**
 * FileStorageAutoConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/3/9 11:50
 * @description:
 */
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {

    @Bean
    public FileStorage fileStorage(FileStorageProperties properties) {
        FileStorageType type = properties.getType();
        FileStorageProperties.Config config = Optional.ofNullable(properties.getConfig())
                .orElseThrow(() -> new FileStorageException(CommonResultStatus.OSS_CONFIG_ERROR));
        switch (type) {
            case OSS:
                return new AliOssStorage(config.getDomain(), config.isHttps(), config.getEndpoint(),
                        config.getAccessKey(), config.getSecretKey(), config.getBucketName());
            case OBS:
                return new HaWeiObsStorage(config.getDomain(), config.isHttps(), config.getEndpoint(),
                        config.getAccessKey(), config.getSecretKey(), config.getBucketName());
            default:
                throw new FileStorageException(CommonResultStatus.OSS_CONFIG_ERROR);
        }
    }
}
