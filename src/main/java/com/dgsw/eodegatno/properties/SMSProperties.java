// SMSProperties.java
package com.dgsw.eodegatno.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coolsms")
public class SMSProperties {
    private String apiKey;
    private String apiSecretKey;
    private String from;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getApiSecretKey() { return apiSecretKey; }
    public void setApiSecretKey(String apiSecretKey) { this.apiSecretKey = apiSecretKey; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
}