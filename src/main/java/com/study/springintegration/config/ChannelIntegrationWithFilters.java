package com.study.springintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
@EnableIntegration
public class ChannelIntegrationWithFilters {

    public String INPUT_DIR = "source/transformer";
    public String OUTPUT_DIR = "destination/transformer";
    public String FILE_PATTERN = "*.txt";

    private MessageChannel fileChannel2(){
        return new DirectChannel();
    }

    private MessageChannel output(){
        return new DirectChannel();
    }

    //creates a channel for file, file read is fed to the channel as a message
    @InboundChannelAdapter(value = "fileChannel2", poller = @Poller(fixedDelay = "50000"))
    public Message<File> readFile(){
        return fileReadingMessageSource().receive();
    }

    //reads the source file and process it
    public FileReadingMessageSource fileReadingMessageSource(){
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(INPUT_DIR));
        source.setFilter(fileFilters());
        System.out.println("JPG Channel Input");
        return source;
    }

    public FileListFilter<File> fileFilters(){
        return new SimplePatternFileListFilter(FILE_PATTERN);
    }

    //reads the message from channel, transform the text in file and sends to the output channel
    @Bean
    @Transformer(inputChannel = "fileChannel2", outputChannel = "output")
    public TextTransformer transform(){
        return new TextTransformer();
    }

    //reads the output of transformer from channel and using handler writes the content to a new file
    //and also update the file name
    @Bean
    @ServiceActivator(inputChannel = "output")
    public FileWritingMessageHandler fileWritingMessageHandler(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
       /* handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                return "Output File.txt";
            }
        });*/
        handler.setFileNameGenerator(fileNameGenerator -> "Output File.txt");
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);//no output-channel or replyChannel header available hence we set
        return handler;
    }

}
