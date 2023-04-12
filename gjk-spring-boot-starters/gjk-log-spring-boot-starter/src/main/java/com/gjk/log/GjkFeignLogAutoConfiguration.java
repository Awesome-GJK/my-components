package com.gjk.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gjk.log.config.LogProperties;
import com.gjk.log.feign.GjkFeignLoggerFactory;

/**
 * GjkFeignLogAutoConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:55
 * @description:
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnProperty(prefix = "gjk.log", name = "feign.enable", havingValue = "true")
public class GjkFeignLogAutoConfiguration {

    @Bean
    public FeignLoggerFactory orderFeignLoggerFactory(LogProperties logProperties) {
        return new GjkFeignLoggerFactory(logProperties);
    }
}
