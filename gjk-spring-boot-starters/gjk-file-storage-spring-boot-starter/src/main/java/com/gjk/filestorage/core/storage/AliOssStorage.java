package com.gjk.filestorage.core.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.gjk.filestorage.core.FileStorage;
import com.gjk.filestorage.enums.CommonResultStatus;
import com.gjk.filestorage.exception.FileStorageException;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AliOssStorage
 *
 * @author: gaojiankang
 * @date: 2023/3/9 11:55
 * @description:
 */
@Slf4j
@AllArgsConstructor
public class AliOssStorage implements FileStorage {

    private final String domain;
    private final boolean https;
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucketName;

    /**
     * 构建OSS
     *
     * @return
     */
    public OSS getOss() {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setSupportCname(true);
        return new OSSClientBuilder().build(endpoint, accessKey, secretKey, conf);
    }

    /**
     * 关闭
     */
    public void shutdown(OSS oss) {
        if (oss != null) {
            oss.shutdown();
        }
    }

    @Override
    public String upload(InputStream inputStream, String keyName) {
        OSS oss = getOss();
        try {
            oss.putObject(bucketName, keyName, inputStream);
        } catch (Exception ex) {
            log.error("文件上传失败，keyName：{}", keyName, ex);
            throw new FileStorageException(CommonResultStatus.OSS_UPLOAD_ERROR);
        } finally {
            IoUtil.close(inputStream);
            shutdown(oss);
        }
        return getUrl(keyName);
    }

    @Override
    public void download(String keyName, OutputStream out) {
        download(keyName, in -> IoUtil.copy(in, out));
    }

    @Override
    public void download(String keyName, Consumer<InputStream> consumer) {
        OSS oss = getOss();
        try (InputStream in = oss.getObject(bucketName, keyName).getObjectContent()) {
            consumer.accept(in);
        } catch (Exception e) {
            log.error("文件下载失败，keyName：{}", keyName, e);
            throw new FileStorageException(CommonResultStatus.OSS_DOWNLOAD_ERROR);
        } finally {
            shutdown(oss);
        }
    }

    @Override
    public String getUrl(String keyName) {
        if (StringUtils.hasText(domain)) {
            return domain + "/" + keyName;
        }
        return (https ? "https://" : "http://") + bucketName + "." + endpoint + "/" + keyName;
    }

    @Override
    public void batchDelete(List<String> keyNames) {
        if (CollectionUtils.isEmpty(keyNames)) {
            return;
        }

        OSS oss = getOss();
        try {
            oss.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keyNames));
        } catch (Exception e) {
            log.error("文件批量删除失败，keyNames：{}", keyNames, e);
            throw new FileStorageException(CommonResultStatus.OSS_DELETE_ERROR);
        } finally {
            shutdown(oss);
        }
    }
}
