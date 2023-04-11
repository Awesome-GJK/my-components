package com.gjk.log.feign;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.gjk.log.config.LogProperties;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.RequiredArgsConstructor;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

/**
 * GjkFeignLogger
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:48
 * @description:
 */
@RequiredArgsConstructor
public class GjkFeignLogger extends feign.Logger {

    private final Logger logger;
    private final LogProperties.FeignConfig feignConfig;

    @Override
    protected void log(String s, String s1, Object... objects) {

    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        try {
            String tag = methodTag(configKey);
            String url = request.url();
            String httpMethod = request.httpMethod().name();
            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                String headers = JSON.toJSONString(request.headers());
                if (request.body() != null && logLevel.ordinal() >= Level.FULL.ordinal()) {
                    String body = request.charset() != null
                            ? new String(request.body(), request.charset()) : "";
                    logger.info("{}{} {}, requestHeaders: {}, requestBody: {}", tag, httpMethod, url, headers, body);
                } else {
                    logger.info("{}{} {}, requestHeaders: {}", tag, httpMethod, url, headers);
                }
            } else {
                logger.info("{}{} {}", tag, httpMethod, url);
            }
        } catch (Exception e) {
            logger.error("打印 Feign 日志异常", e);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if (!logger.isInfoEnabled()) {
            return response;
        }

        try {
            String tag = methodTag(configKey);
            String reason = Optional.ofNullable(response.reason()).orElse("");
            int status = response.status();
            if (logLevel.ordinal() >= Level.HEADERS.ordinal() && feignConfig.isRecordResponse()) {
                String headers = JSON.toJSONString(response.headers());
                if (response.body() != null && !(status == 204 || status == 205)) {
                    byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                    if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyData.length > 0) {
                        String body = decodeOrDefault(bodyData, UTF_8, "");
                        logger.info("{}{} {}, cost: {}ms, responseHeaders: {}, responseBody: {}", tag, status, reason, elapsedTime, headers, body);
                    } else {
                        logger.info("{}{} {}, cost: {}ms, responseHeaders: {}", tag, status, reason, elapsedTime, headers);
                    }
                    return response.toBuilder().body(bodyData).build();
                } else {
                    logger.info("{}{} {}, cost: {}ms, responseHeaders: {}", tag, status, reason, elapsedTime, headers);
                }
            } else {
                logger.info("{}{} {}, cost: {}ms", tag, status, reason, elapsedTime);
            }
        } catch (Exception e) {
            logger.error("打印 Feign 日志异常", e);
        }
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        String tag = methodTag(configKey);
        logger.error("{}cost: {}ms, errMsg: {}: {}", tag, elapsedTime, ioe.getClass(), ioe.getMessage());
        return ioe;
    }
}
