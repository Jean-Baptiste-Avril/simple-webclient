package com.jbavril.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DirectConfig {

    @Value("${direct.url.api}")
    private String urlHttp;

    @Bean("direct-http")
    public WebClient getDirectWebClient() {
        return WebClient.builder()
                .baseUrl(urlHttp)
                .build();
    }

}