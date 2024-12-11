package com.securechat.wisweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "google.recaptcha")
@Data
public class RecaptchaConfig {
    private String siteKey;
    private String secretKey;
    private String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
}