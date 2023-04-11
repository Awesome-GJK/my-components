package com.gjk.log.feign;

import com.alibaba.fastjson.JSON;

import lombok.Builder;
import lombok.Data;

/**
 * GjkFeignLog
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:46
 * @description:
 */
@Data
@Builder
public class GjkFeignLog {

    /**
     * 客户端请求IP
     */
    private String ip;

    /**
     * 请求url
     */
    private String apiUrl;

    /**
     * 操作用户的ID
     */
    private String operatorId;

    /**
     * 请求耗时
     */
    private String consumingTime;

    /**
     * http 请求方式
     */
    private String httpMethod;

    /**
     * 访问接口类名称和方法
     */
    private String apiClassMethodName;

    /**
     * 请求参数体 (JSON格式)
     */
    private String reqParam;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 请求返回体
     */
    private Object resp;

    /**
     * 转 Json
     *
     * @return
     */
    public String toJson() {
        return JSON.toJSONString(this);
    }
}
