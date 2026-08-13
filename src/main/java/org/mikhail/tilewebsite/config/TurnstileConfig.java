package org.mikhail.tilewebsite.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudflare.turnstile")
public class TurnstileConfig {

    @Value("${cloudflare.turnstile.site-key}")
    private String siteKey;

    @Value("${cloudflare.turnstile.secret-key}")
    private String secretKey;

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
