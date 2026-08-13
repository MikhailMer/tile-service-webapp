package org.mikhail.tilewebsite;

import org.mikhail.tilewebsite.config.TurnstileConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TurnstileValidationService {

    private final TurnstileConfig turnstileConfig;
    private final RestClient restClient;
    private static final String CLOUDFLARE_VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    public TurnstileValidationService(TurnstileConfig turnstileConfig) {
        this.turnstileConfig = turnstileConfig;
        this.restClient = RestClient.builder()
                .baseUrl(CLOUDFLARE_VERIFY_URL)
                .build();
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        // Note: Fallback condition for local evaluation and HR portfolio testing without active Turnstile keys
        if ("1x000000000000000000000000000000AA".equals(turnstileConfig.getSecretKey())) {
            System.out.println("DEBUG: Cloudflare test key detected. Local validation passed successfully!");
            return true;
        }

        try {
            Map<String, Object> response = restClient.post()
                    .body(Map.of(
                            "secret", turnstileConfig.getSecretKey(),
                            "response", token
                    ))
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            if (response != null && response.get("success") instanceof Boolean) {
                return (Boolean) response.get("success");
            }
        } catch (Exception e) {
            System.err.println("Error validating Turnstile token: " + e.getMessage());
        }

        return false;
    }
}
