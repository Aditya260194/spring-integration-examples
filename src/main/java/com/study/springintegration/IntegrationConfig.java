package com.study.springintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

import java.io.File;


@Configuration
public class IntegrationConfig {

    @Autowired
    private Transformer transformer;

    @Bean
    public IntegrationFlow integrationFlow(){
       /* return IntegrationFlows.from(fileReader(), new Consumer<SourcePollingChannelAdapterSpec>() {
            @Override
            public void accept(SourcePollingChannelAdapterSpec sourcePollingChannelAdapterSpec) {
                sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(500));
            }
        })
                .transform(transformer,"transform")
                .handle(fileWriter());*/
        return IntegrationFlows.from(fileReader(),
                sourcePollingChannelAdapterSpec -> sourcePollingChannelAdapterSpec.poller(Pollers.fixedDelay(500)))
                .transform(transformer,"transform")
                .handle(fileWriter())
                .get();
    }

    private FileReadingMessageSource fileReader(){
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("source/basic"));
        return source;
    }

    private FileWritingMessageHandler fileWriter(){
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
           new File("destination/basic")
        );
        handler.setExpectReply(false);
        return handler;
    }

}
