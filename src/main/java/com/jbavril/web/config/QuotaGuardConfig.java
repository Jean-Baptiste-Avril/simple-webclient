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
public class QuotaGuardConfig {
    
    @Value("${quotaguard.url.http}")
    private String urlQuotaGuardHttp;

    @Value("${quotaguard.url.https}")
    private String urlQuotaGuardHttps;

    @Value("${quotaguard.url.api}")
    private String urlQuotaGuardApi;

    @Bean("quotaguard-http")
    public WebClient getQuotaGuardHttpWebClient() throws MalformedURLException {
        return getQuotaGuardWebClient(urlQuotaGuardHttp);
    }

    @Bean("quotaguard-https")
    public WebClient getQuotaGuardHttpsWebClient() throws MalformedURLException {
		return getQuotaGuardWebClient(urlQuotaGuardHttps);
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
