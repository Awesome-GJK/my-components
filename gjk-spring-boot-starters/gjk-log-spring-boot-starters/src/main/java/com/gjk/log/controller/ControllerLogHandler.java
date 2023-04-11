package com.gjk.log.controller;

import javax.servlet.http.HttpServletRequest;

import com.gjk.log.config.LogProperties;

/**
 * ControllerLogHandler
 *
 * @author: gaojiankang
 * @date: 2023/4/10 19:17
 * @description:
 */
public interface ControllerLogHandler {

    /**
     * 处理日志
     * @param requestLog
     * @param config
     */
    void handle(ControllerLog requestLog, LogProperties.ControllerConfig config);

    /**
     * 获取操作人标识
     *
     * @param request
     * @return
     */
    String getOperatorId(HttpServletRequest request);
}
