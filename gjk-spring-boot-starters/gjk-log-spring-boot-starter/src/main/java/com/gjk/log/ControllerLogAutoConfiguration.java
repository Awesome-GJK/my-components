package com.gjk.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gjk.log.config.LogProperties;
import com.gjk.log.controller.ControllerLogAspect;
import com.gjk.log.controller.ControllerLogHandler;
import com.gjk.log.controller.DefaultControllerLogHandler;

/**
 * ControllerLogAutoConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:41
 * @description:
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnProperty(prefix = "gjk.log", name = "controller.enable", havingValue = "true")
public class ControllerLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ControllerLogHandler.class)
    public ControllerLogHandler requestLogHandler() {
        return new DefaultControllerLogHandler();
    }

    @Bean
    public ControllerLogAspect controllerLogAspect(ControllerLogHandler logHandler, LogProperties properties) {
        return new ControllerLogAspect(logHandler, properties.getController());
    }
}
