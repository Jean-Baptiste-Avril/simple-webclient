package com.jbavril.web.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

import com.jbavril.web.service.DirectService;
import com.jbavril.web.service.FixieService;
import com.jbavril.web.service.QuotaGuardService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/v1/ip")
public class IpController {

    private static final Logger LOG = LoggerFactory.getLogger(IpController.class);

    @Autowired
    private QuotaGuardService quotaGuardService;

    @Autowired
    private FixieService fixieService;

    @Autowired
    private DirectService directService;

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

    @GetMapping("/http/fixie/get")
    public ResponseEntity<String> getIpWthHttpFixieProxy() {
        String response = fixieService.getIpWithHttp();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/http/test/get")
    public ResponseEntity<String> getIpWthHttpTestProxy() {
        String response = "";
        LocalDateTime debut;

        // Fixie
        debut = LocalDateTime.now();
        IntStream.range(0, 10).forEach(i -> {
            LOG.info("Appel FIXIE: {}", i);
            fixieService.getIpWithHttp();
        });
        response += "Temps fixie: " + ChronoUnit.MILLIS.between(debut, LocalDateTime.now()) + "ms \n";

        // Quotaguard 
        debut = LocalDateTime.now();
        IntStream.range(0, 10).forEach(i -> {
            LOG.info("Appel QuotaGuard: {}", i);
            quotaGuardService.getIpWithHttp();
        }); 
        response += "Temps quotaguard: "+ ChronoUnit.MILLIS.between(debut, LocalDateTime.now()) + "ms \n";

        // Direct 
        debut = LocalDateTime.now();
        IntStream.range(0, 10).forEach(i -> {
            LOG.info("Appel Direct: {}", i);
            directService.getIpWithHttp();
        }); 
        response += "Temps direct: "+ ChronoUnit.MILLIS.between(debut, LocalDateTime.now()) + "ms \n";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}