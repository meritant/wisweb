package com.securechat.wisweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.securechat.wisweb.config.RecaptchaConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Service
public class CaptchaService {
    
    @Autowired
    private RecaptchaConfig recaptchaConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();

    @Data
    private static class RecaptchaResponse {
        private boolean success;
        @JsonProperty("error-codes")
        private String[] errorCodes;
    }

//    public boolean validateCaptcha(String captchaResponse) {
//        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
//        requestMap.add("secret", recaptchaConfig.getSecretKey());
//        requestMap.add("response", captchaResponse);
//
//        ResponseEntity<RecaptchaResponse> response = restTemplate.postForEntity(
//            recaptchaConfig.getVerifyUrl(),
//            requestMap,
//            RecaptchaResponse.class
//        );
//
//        return response.getBody() != null && response.getBody().isSuccess();
//    }
    
    
    public boolean validateCaptcha(String captchaResponse) {
        try {
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
            requestMap.add("secret", recaptchaConfig.getSecretKey());
            requestMap.add("response", captchaResponse);

            System.out.println("Validating CAPTCHA with secret: " + recaptchaConfig.getSecretKey()); // Debug log

            ResponseEntity<RecaptchaResponse> response = restTemplate.postForEntity(
                recaptchaConfig.getVerifyUrl(),
                requestMap,
                RecaptchaResponse.class
            );

            System.out.println("CAPTCHA response: " + response.getBody()); // Debug log

            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return false;
        }
    }
}