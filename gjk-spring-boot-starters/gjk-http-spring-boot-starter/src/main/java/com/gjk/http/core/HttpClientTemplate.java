package com.gjk.http.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.gjk.http.exception.CommonResultStatus;
import com.gjk.http.exception.HttpException;

import cn.hutool.core.date.StopWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HttpClientTemplate
 *
 * @author: gaojiankang
 * @date: 2023/4/11 19:30
 * @description:
 */
@Slf4j
@RequiredArgsConstructor
public class HttpClientTemplate implements HttpTemplate {

    private final CloseableHttpClient httpClient;
    private final String defaultCharset;

    @Override
    public String httpGet(String url) {
        return httpGet(url, null);
    }

    @Override
    public String httpGet(String url, Map<String, String> params) {
        return httpGet(url, params, null, defaultCharset);
    }

    @Override
    public String httpGet(String url, Map<String, String> params, Map<String, String> headers, String charset) {
        StopWatch watch = new StopWatch();
        watch.start();
        CloseableHttpResponse resp = null;
        String respondBody;
        try {
            HttpGet httpGet = new HttpGet(buildUri(params, url));
            setHeaders(headers, httpGet);
            resp = httpClient.execute(httpGet);
            respondBody = EntityUtils.toString(resp.getEntity(), getCharset(charset));
        } catch (HttpException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            watch.stop();
            log.error("调用【GET:{}】超时，耗时：{}，请求参数：{}", url, watch.getTotalTimeMillis(), JSON.toJSONString(params), e);
            throw new HttpException(CommonResultStatus.REQUEST_TIMEOUT);
        } catch (Exception e) {
            log.error("调用【GET:{}】失败，请求参数：{}", url, JSON.toJSONString(params), e);
            throw new HttpException(CommonResultStatus.REQUEST_ERROR);
        } finally {
            release(resp);
        }
        watch.stop();
        if (log.isDebugEnabled()) {
            log.debug("调用【GET:{}】成功，耗时：{}，响应参数：{}", url, watch.getTotalTimeMillis(), respondBody);
        }
        return respondBody;
    }

    @Override
    public String httpPost(String url, String json) {
        return httpPost(url, json, null);
    }

    @Override
    public String httpPost(String url, String json, Map<String, String> headers) {
        return httpPost(url, json, headers, defaultCharset);
    }

    @Override
    public String httpPost(String url, String json, Map<String, String> headers, String charset) {
        StopWatch watch = new StopWatch();
        watch.start();
        String responseBody;
        CloseableHttpResponse response = null;
        try {
            Charset cs = getCharset(charset);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, cs));
            headers = Optional.ofNullable(headers).orElse(new HashMap<>(2));
            headers.putIfAbsent(HttpHeaders.CONTENT_TYPE, "application/json");
            setHeaders(headers, httpPost);
            response = httpClient.execute(httpPost);
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (HttpException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            watch.stop();
            log.error("调用【POST:{}】超时，耗时：{}，请求参数：{}", url, watch.getTotalTimeMillis(), json, e);
            throw new HttpException(CommonResultStatus.REQUEST_TIMEOUT);
        } catch (Exception e) {
            log.error("调用【POST:{}】失败，请求参数：{}", url, json, e);
            throw new HttpException(CommonResultStatus.REQUEST_ERROR);
        } finally {
            release(response);
        }
        watch.stop();
        if (log.isDebugEnabled()) {
            log.debug("调用【POST:{}】成功，耗时：{}，响应参数：{}", url, watch.getTotalTimeMillis(), responseBody);
        }
        return responseBody;
    }

    @Override
    public String httpPost(String url, Map<String, String> params) {
        return null;
    }

    @Override
    public String httpPost(String url, Map<String, String> params, Map<String, String> headers) {
        return null;
    }

    @Override
    public String httpPost(String url, Map<String, String> params, Map<String, String> headers, String charset) {
        StopWatch watch = new StopWatch();
        watch.start();
        CloseableHttpResponse response = null;
        String responseBody;
        try {
            Charset cs = getCharset(charset);
            HttpPost httpPost = new HttpPost(url);
            setHeaders(headers, httpPost);
            setParams(params, httpPost, cs);
            response = httpClient.execute(httpPost);
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (HttpException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            watch.stop();
            log.error("调用【POST:{}】超时，耗时：{}，请求参数：{}", url, watch.getTotalTimeMillis(), JSON.toJSONString(params), e);
            throw new HttpException(CommonResultStatus.REQUEST_TIMEOUT);
        } catch (Exception e) {
            log.error("调用【POST:{}】失败，请求参数：{}", url, JSON.toJSONString(params), e);
            throw new HttpException(CommonResultStatus.REQUEST_ERROR);
        } finally {
            release(response);
        }
        watch.stop();
        if (log.isDebugEnabled()) {
            log.debug("调用【POST:{}】成功，耗时：{}，响应参数：{}", url, watch.getTotalTimeMillis(), responseBody);
        }
        return responseBody;
    }

    /**
     * 设置参数
     *
     * @param params
     * @param httpMethod
     * @param cs
     */
    private void setParams(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod, Charset cs) {
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            httpMethod.setEntity(new UrlEncodedFormEntity(pairs, cs));
        }
    }

    /**
     * 将参数拼装到 url
     *
     * @param params
     * @param url
     * @return
     */
    private URI buildUri(Map<String, String> params, String url) {
        URI finalUrl;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            finalUrl = uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("请求【{}】参数拼接失败", url, e);
            throw new HttpException(CommonResultStatus.REQUEST_ERROR);
        }
        return finalUrl;
    }

    /**
     * 设置请求头
     *
     * @param headers
     * @param httpMethod
     */
    private void setHeaders(Map<String, String> headers, HttpRequestBase httpMethod) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                Optional.ofNullable(entry.getValue())
                        .ifPresent(value -> httpMethod.setHeader(entry.getKey(), value));
            }
        }
    }


    /**
     * 获取 charset
     *
     * @param charset
     * @return
     */
    private Charset getCharset(String charset) {
        charset = StringUtils.isNotBlank(charset) ? charset : defaultCharset;
        return Charset.forName(charset);
    }

    /**
     * 释放资源
     *
     * @param httpResponse
     */
    private void release(CloseableHttpResponse httpResponse) {
        try {
            if (httpResponse != null) {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (IOException e) {
            log.error("关闭 HTTP 响应时抛出异常，需要关注", e);
        }
    }
}
