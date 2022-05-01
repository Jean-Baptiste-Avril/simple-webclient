package com.jbavril.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

@Service
public class QuotaGuardService {

    @Autowired
    @Qualifier("quotaguard-http")
    private WebClient webClientHttp;
    
    @Autowired
    @Qualifier("quotaguard-https")
    private WebClient webClientHttps;

    public String getIpWithHttp() {
        RequestHeadersSpec<?> request = webClientHttp.get().uri("");
		return request.retrieve().bodyToMono(String.class).block();
    }

    public String getIpWithHttps() {
        RequestHeadersSpec<?> request = webClientHttps.get().uri("");
		return request.retrieve().bodyToMono(String.class).block();
    }


}
