package com.study.springintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
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
//annotation designates this class as a Spring Integration configuration.
public class DirectChannelIntegrationConfig {

    public String INPUT_DIR = "source/directchannel";
    public String OUTPUT_DIR = "destination/directchannel";
    public String FILE_PATTERN = "*.jpg";

    @Bean
    public MessageChannel fileChannel(){
        return new DirectChannel();
        //one to one channel
    }

    @Bean
    @InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
    public FileReadingMessageSource readFileMessageSource(){
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(INPUT_DIR));
        source.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
        System.out.println("JPG Channel Input");
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "fileChannel")
    public FileWritingMessageHandler writeFileMessageHandler(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        System.out.println("JPG Channel Output");
        return handler;
    }

}
