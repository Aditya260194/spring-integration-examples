package com.study.springintegration;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Transformer {

    public String transform(String filePath) throws IOException {
        System.out.println("File path : "+ filePath);
        String content = new String(Files.readAllBytes(
                Paths.get(filePath)));
        return "Transformed:"+content;
    }
}
