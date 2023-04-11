package com.gjk.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * LogProperties
 *
 * @author: gaojiankang
 * @date: 2023/4/10 19:18
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "gjk.log")
public class LogProperties {

    /**
     * controller 日志切面配置
     */
    private ControllerConfig controller = new ControllerConfig();

    /**
     * 自定义 feign 日志配置
     */
    private FeignConfig feign = new FeignConfig();

    @Data
    public static class ControllerConfig {

        /**
         * 是否开启 controller 日志切面
         */
        private boolean enable = false;

        /**
         * 是否记录返回参数
         */
        private boolean recordResponse = false;

        /**
         * 是否忽略 GET 请求
         */
        private boolean ignoreGetMethod = true;
    }

    @Data
    public static class FeignConfig {

        /**
         * 是否开启自定义 Feign 日志
         */
        private boolean enable = false;

        /**
         * 是否记录返回参数
         */
        private boolean recordResponse = false;
    }
}
