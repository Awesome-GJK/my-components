package com.gjk.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
@EnableConfigurationProperties(LogProperties.class)
public class ControllerLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ControllerLogHandler.class)
    @ConditionalOnProperty(name = "gjk.log.controller.enable", havingValue = "true")
    public ControllerLogHandler requestLogHandler() {
        return new DefaultControllerLogHandler();
    }

    @Bean
    @ConditionalOnProperty(name = "gjk.log.controller.enable", havingValue = "true")
    public ControllerLogAspect controllerLogAspect(ControllerLogHandler logHandler, LogProperties properties) {
        return new ControllerLogAspect(logHandler, properties.getController());
    }
}
