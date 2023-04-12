package com.gjk.http.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.Args;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.gjk.http.core.HttpClientTemplate;
import com.gjk.http.core.HttpTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * HttpClientConfiguration
 *
 * @author: gaojiankang
 * @date: 2023/4/12 11:08
 * @description:
 */
@Slf4j
@EnableConfigurationProperties(HttpClientProperties.class)
@ConditionalOnClass(HttpClient.class)
@ConditionalOnProperty(prefix = "gjk.http.httpclient", name = "enabled", havingValue = "true")
public class HttpClientConfiguration {

    @Bean
    @ConditionalOnMissingBean(HttpTemplate.class)
    public HttpTemplate httpTemplate(CloseableHttpClient httpClient, HttpClientProperties properties) {
        return new HttpClientTemplate(httpClient, properties.getCharset());
    }

    /**
     * 配置 httpClientBuilder
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpClientBuilder httpClientBuilder() {
        return HttpClientBuilder.create();
    }

    /**
     * 配置 httpClient
     *
     * @param httpClientProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public CloseableHttpClient httpClient(HttpClientProperties httpClientProperties,
                                          HttpClientBuilder httpClientBuilder,
                                          ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        try {
            // ssl 信任配置
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory)
                    .build();

            // 连接池配置
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry, null, null, null,
                    httpClientProperties.getTimeToLive(), TimeUnit.SECONDS);
            connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
            connectionManager.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoute());

            // httpClient 配置
            httpClientBuilder.setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimout())
                            .setConnectTimeout(httpClientProperties.getConnectTimeout())
                            .setSocketTimeout(httpClientProperties.getReadTimeout()).build())
                    .evictExpiredConnections()
                    .evictIdleConnections(httpClientProperties.getIdleTimeout(), TimeUnit.SECONDS)
                    .setKeepAliveStrategy(connectionKeepAliveStrategy);
            if (httpClientProperties.getRetryTimes() > 0) {
                httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientProperties.getRetryTimes(), false));
            }
            CloseableHttpClient httpClient = httpClientBuilder.build();

            // 关闭连接池钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("开始关闭 httpclient 连接池...");
                    httpClient.close();
                    connectionManager.close();
                } catch (IOException e) {
                    log.error("连接池关闭失败", e);
                }
            }));
            return httpClient;
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("初始化 httpclient 出错", e);
            throw new BeanCreationException(e.getMessage());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(HttpClientProperties httpClientProperties) {
        return (response, context) -> {
            Args.notNull(response, "HTTP response");
            final HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                final HeaderElement he = it.nextElement();
                final String param = he.getName();
                final String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch (final NumberFormatException ignore) {
                    }
                }
            }
            return httpClientProperties.getKeepAliveTime() * 1000;
        };
    }
}
