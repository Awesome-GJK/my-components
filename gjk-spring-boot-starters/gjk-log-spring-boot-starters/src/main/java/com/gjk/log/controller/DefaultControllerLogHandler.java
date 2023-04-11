package com.gjk.log.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;

import com.gjk.log.config.LogProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * DefaultControllerLogHandler
 *
 * @author: gaojiankang
 * @date: 2023/4/10 19:22
 * @description:
 */
@Slf4j
public class DefaultControllerLogHandler implements ControllerLogHandler{
    @Override
    public void handle(ControllerLog requestLog, LogProperties.ControllerConfig config) {
        if (HttpMethod.GET.name().equals(requestLog.getHttpMethod())) {
            if (!config.isIgnoreGetMethod() && log.isDebugEnabled()) {
                log.debug("ControllerLog: {}", requestLog.toJson());
            }
        } else {
            log.info("ControllerLog: {}", requestLog.toJson());
        }
    }

    @Override
    public String getOperatorId(HttpServletRequest request) {
        return null;
    }
}
