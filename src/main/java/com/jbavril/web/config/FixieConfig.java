package com.jbavril.web.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class FixieConfig {
    
    @Value("${fixie.url.http}")
    private String urlQuotaGuardHttp;

    @Value("${fixie.url.api}")
    private String urlQuotaGuardApi;

    @Bean("fixie-http")
    public WebClient getQuotaGuardHttpWebClient() throws MalformedURLException {
        return getQuotaGuardWebClient(urlQuotaGuardHttp);
    }

    private WebClient getQuotaGuardWebClient(String quotaGuardUrl) throws MalformedURLException {
        URL proxyUrl = new URL(quotaGuardUrl);

		String userInfo = proxyUrl.getUserInfo();
		String proxyUser = userInfo.substring(0, userInfo.indexOf(':'));
		String proxyPassword = userInfo.substring(userInfo.indexOf(':') + 1);

		HttpClient httpClient = HttpClient.create().resolver(spec -> spec.queryTimeout(Duration.ofMillis(500)))
				.proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host(proxyUrl.getHost()).port(proxyUrl.getPort())
						.username(proxyUser).password(passwd -> proxyPassword));

		return WebClient.builder()
				.baseUrl(urlQuotaGuardApi)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
    }

}