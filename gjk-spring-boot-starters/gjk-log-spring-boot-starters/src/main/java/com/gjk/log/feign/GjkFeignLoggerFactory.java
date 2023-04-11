package com.gjk.log.feign;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;

import com.gjk.log.config.LogProperties;

import feign.Logger;
import lombok.RequiredArgsConstructor;

/**
 * FeignLoggerFactory
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:51
 * @description:
 */
@RequiredArgsConstructor
public class GjkFeignLoggerFactory implements FeignLoggerFactory {

    private final LogProperties logProperties;

    @Override
    public Logger create(Class<?> type) {
        return new GjkFeignLogger(LoggerFactory.getLogger(type), logProperties.getFeign());
    }
}
