package com.example.spring_integration;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

@Configuration
@EnableIntegration
public class SpringIntegrationConfig {

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
    public FileReadingMessageSource fileReadingMessageSource() {

        // filter configuration to read a file
        CompositeFileListFilter<File> filter=new CompositeFileListFilter<>();
        filter.addFilter(new SimplePatternFileListFilter("*.png")); // for seperating png files only
        
        FileReadingMessageSource readder = new FileReadingMessageSource();
        readder.setDirectory(new File("src/main/resources/Source"));
        readder.setFilter(filter);
        return readder;
    }

    @Bean
    @ServiceActivator(inputChannel = "fileInputChannel")
    public FileWritingMessageHandler fileWritingMessageHandler() {

        FileWritingMessageHandler writter = new FileWritingMessageHandler(new File("src/main/resources/Destination"));
        writter.setAutoCreateDirectory(true);
        writter.setExpectReply(false);
        return writter;
    }
}
