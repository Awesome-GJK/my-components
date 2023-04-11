package com.gjk.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;

import com.gjk.log.feign.GjkFeignLoggerFactory;

/**
 * GjkFeignLogAutoConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:55
 * @description:
 */
@EnableConfigurationProperties(LogProperties.class)
public class GjkFeignLogAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "gjk.log.feign.enable", havingValue = "true")
    public FeignLoggerFactory orderFeignLoggerFactory(LogProperties logProperties) {
        return new GjkFeignLoggerFactory(logProperties);
    }
}
