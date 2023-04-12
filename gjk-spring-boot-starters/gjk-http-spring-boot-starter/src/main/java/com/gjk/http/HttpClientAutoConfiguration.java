package com.gjk.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gjk.http.config.HttpClientConfiguration;

/**
 * HttpClientAutoConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/4/12 13:41
 * @description:
 */
@Configuration
@Import({HttpClientConfiguration.class})
public class HttpClientAutoConfiguration {
}
