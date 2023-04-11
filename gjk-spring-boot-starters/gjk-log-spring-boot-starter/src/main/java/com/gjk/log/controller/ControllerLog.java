package com.gjk.log.controller;

import com.alibaba.fastjson.JSON;

import lombok.Builder;
import lombok.Data;

/**
 * ControllerLog
 *
 * @author: gaojiankang
 * @date: 2023/4/10 19:11
 * @description:
 */
@Data
@Builder
public class ControllerLog {

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
