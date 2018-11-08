package com.mailian.core.config;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/8
 * @Description:
 */
@Configuration
public class RestClientConfig {
    private static final Logger log = LoggerFactory.getLogger(RestClientConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).build();
        // 开始设置连接池
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(500); // 最大连接数500
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100); // 同路由并发数100
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)); // 重试次数
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient); // httpClient连接配置
        clientHttpRequestFactory.setConnectTimeout(20000);              // 连接超时
        clientHttpRequestFactory.setReadTimeout(5000);                 // 数据读取超时时间
        clientHttpRequestFactory.setConnectionRequestTimeout(20000);    // 连接不够用的等待时间
        return clientHttpRequestFactory;
    }
}
