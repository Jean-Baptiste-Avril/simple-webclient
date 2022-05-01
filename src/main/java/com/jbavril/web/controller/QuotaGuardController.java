package com.jbavril.web.controller;

import com.jbavril.web.service.QuotaGuardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/v1/ip")
public class QuotaGuardController {
    
    @Autowired
    private QuotaGuardService quotaGuardService;
    
    @GetMapping("/https/get")
    public ResponseEntity<String> getIpWthHttpsProxy() {
        String response = quotaGuardService.getIpWithHttps();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/http/get")
    public ResponseEntity<String> getIpWthHttpProxy() {
        String response = quotaGuardService.getIpWithHttp();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
