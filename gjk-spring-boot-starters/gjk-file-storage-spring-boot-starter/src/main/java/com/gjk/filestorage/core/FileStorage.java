package com.gjk.filestorage.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * FileStorage
 *
 * @author: gaojiankang
 * @date: 2023/3/9 11:55
 * @description:
 */
public interface FileStorage {

    /**
     * 上传
     *
     * @param inputStream 文件输入流
     * @param keyName     带根路径的文件名
     * @return
     */
    String upload(InputStream inputStream, String keyName);

    /**
     * 下载到指定 OutputStream
     *
     * @param keyName 带根路径的文件名
     * @param out     文件输出流
     */
    void download(String keyName, OutputStream out);

    /**
     * 下载
     *
     * @param keyName  带根路径的文件名
     * @param consumer 消费函数
     * @return
     */
    void download(String keyName, Consumer<InputStream> consumer);

    /**
     * 获取 url
     *
     * @param keyName 带根路径的文件名
     * @return
     */
    String getUrl(String keyName);

    /**
     * 批量删除
     *
     * @param keyNames 待删除的 key 列表
     */
    void batchDelete(List<String> keyNames);
}
