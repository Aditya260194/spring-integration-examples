package com.study.springintegration.config;

import org.apache.commons.io.FileUtils;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;


import java.io.File;
import java.io.IOException;

public class TextTransformer {

    /*@Transformer
    public Message toRequest(Message<File> message) throws IOException {
        System.out.println("message"+ message.getHeaders().toString());
        File file = message.getPayload();
        System.out.println(file.getName());
        String content = "Transformed by another " + FileUtils.readFileToString(file,"UTF-8");
        //return "Transformed 2:" + content;
        FileUtils.writeStringToFile(file, content, "UTF-8");
        return message;
    }*/

    @Transformer
    public String toRequest(Message<File> message) throws IOException {
        System.out.println("message"+ message.getHeaders().toString());
        File file = message.getPayload();
        System.out.println(file.getName());
        String content = FileUtils.readFileToString(file,"UTF-8");
        return "Transformed 2:" + content;
    }
}
