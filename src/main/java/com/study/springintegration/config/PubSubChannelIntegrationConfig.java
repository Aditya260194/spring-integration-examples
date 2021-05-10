package com.study.springintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
@EnableIntegration
public class PubSubChannelIntegrationConfig {
    public String INPUT_DIR = "source/pubsub";
    public String OUTPUT_DIR = "destination/pubsub";
    public String ARCHIVE_DIR = "archive/pubsub";
    public String FILE_PATTERN2 = "*.png";

    @Bean
    public MessageChannel pubSubFileChannel() {
        return new PublishSubscribeChannel();
        //one-to-many communication
    }

    @Bean
    @InboundChannelAdapter(value = "pubSubFileChannel", poller = @Poller(fixedDelay = "1000"))
    public FileReadingMessageSource readFileMessageSourceForPNG(){
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(INPUT_DIR));
        source.setFilter(new SimplePatternFileListFilter(FILE_PATTERN2));
        System.out.println("PNG Channel Input");
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubSubFileChannel")
    public FileWritingMessageHandler writeFileMessageHandlerForPNG(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        System.out.println("PNG Channel Output - 1");
        return handler;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubSubFileChannel")
    public FileWritingMessageHandler writeFileMessageHandlerForPNG2(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(ARCHIVE_DIR));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        System.out.println("PNG Channel Output - 2");
        return handler;
    }

}
