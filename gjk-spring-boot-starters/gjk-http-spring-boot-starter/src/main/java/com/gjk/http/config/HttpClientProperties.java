package com.gjk.http.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * HttpClientProperties
 *
 * @author: gaojiankang
 * @date: 2023/4/12 11:01
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "gjk.http.httpclient")
public class HttpClientProperties {


    /**
     * 是否开启，默认不开启
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * 最大连接数
     */
    private Integer maxTotal = 500;

    /**
     * 单个路由最大连接数
     */
    private Integer maxPerRoute = 50;

    /**
     * 连接存活时长（单位为秒）
     */
    private Integer timeToLive = 600;

    /**
     * 连接闲置时间，超过该值的连接将被清理（单位秒）
     */
    private Integer idleTimeout = 10;

    /**
     * 长连接保持时间（单位为秒）
     */
    private Integer keepAliveTime = 30;

    /**
     * 连接超时（单位毫秒）
     */
    private Integer connectTimeout = 5 * 1000;

    /**
     * 读取超时（单位毫秒）
     */
    private Integer readTimeout = 8 * 1000;

    /**
     * 从连接池获取连接超时（单位毫秒）
     */
    private int connectionRequestTimout = 2 * 1000;

    /**
     * 字符集
     */
    private String charset = "UTF-8";

    /**
     * 重试次数，默认不重试
     */
    private int retryTimes = 0;
}
