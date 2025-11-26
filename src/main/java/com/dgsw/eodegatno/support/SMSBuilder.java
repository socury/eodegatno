// SMSBuilder.java
package com.dgsw.eodegatno.support;

import com.dgsw.eodegatno.properties.SMSProperties;
import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

@Component
public class SMSBuilder {

    private final DefaultMessageService messageService;
    private final String defaultFrom;

    public SMSBuilder(SMSProperties properties) {
        this.messageService = SolapiClient.INSTANCE.createInstance(
                properties.getApiKey(),
                properties.getApiSecretKey()
        );
        this.defaultFrom = properties.getFrom();
    }

    public Sender to(String to) {
        return new Sender(to);
    }

    public class Sender {
        private final String to;

        private Sender(String to) {
            this.to = to;
        }

        public MultipleDetailMessageSentResponse send(String text) {
            try {
                Message message = new Message();
                message.setFrom(defaultFrom);
                message.setTo(this.to);
                message.setText(text);
                return messageService.send(message);
            } catch (Exception e) {
                throw new RuntimeException("SMS 전송 실패: " + e.getMessage(), e);
            }
        }
    }
}