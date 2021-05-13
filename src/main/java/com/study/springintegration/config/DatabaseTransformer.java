package com.study.springintegration.config;

import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import java.io.IOException;
import java.util.List;

public class DatabaseTransformer {

    @Transformer
    public String toRequest(Message<List<String>> message) throws IOException {
        System.out.println("message"+ message.getHeaders().toString());
        System.out.println("message payload "+ message.getPayload());
        System.out.println("message.getPayload().get(0) "+ message.getPayload().get(0));
        return message.getPayload().get(0) + "1";
    }
}
