package com.gjk.http.core;

import java.util.Map;

/**
 * HttpTemplate
 *
 * @author: gaojiankang
 * @date: 2023/4/11 19:27
 * @description:
 */
public interface HttpTemplate {

    /**
     * get 请求
     *
     * @param url 请求路径
     * @return 响应文本
     */
    String httpGet(String url);

    /**
     * get 请求
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return 响应文本
     */
    String httpGet(String url, Map<String, String> params);

    /**
     * get 请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     * @param charset 字符集
     * @return 响应文本
     */
    String httpGet(String url, Map<String, String> params, Map<String, String> headers, String charset);

    /**
     * post 请求
     *
     * @param url  请求路径
     * @param json json 类型参数
     * @return 响应文本
     */
    String httpPost(String url, String json);

    /**
     * post 请求
     *
     * @param url     请求路径
     * @param json    json 类型参数
     * @param headers 请求头
     * @return 响应文本
     */
    String httpPost(String url, String json, Map<String, String> headers);

    /**
     * post 请求
     *
     * @param url     请求路径
     * @param json    json 类型参数
     * @param headers 请求头
     * @param charset 字符集
     * @return 响应文本
     */
    String httpPost(String url, String json, Map<String, String> headers, String charset);

    /**
     * post 请求
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return 响应文本
     */
    String httpPost(String url, Map<String, String> params);

    /**
     * post 请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应文本
     */
    String httpPost(String url, Map<String, String> params, Map<String, String> headers);

    /**
     * post 请求
     *
     * @param url     请求路径
     * @param params  请求参数
     * @param headers 请求头
     * @param charset 字符集
     * @return 响应文本
     */
    String httpPost(String url, Map<String, String> params, Map<String, String> headers, String charset);
}
