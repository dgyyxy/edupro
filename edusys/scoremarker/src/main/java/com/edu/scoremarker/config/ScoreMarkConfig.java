package com.edu.scoremarker.config;

import com.edu.common.dao.model.EduPaper;
import com.edu.common.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Gary on 2017/6/8.
 */
@Configuration
@ComponentScan("com.edu.scoremarker")
public class ScoreMarkConfig {
    private static final Logger LOGGER = Logger.getLogger(ScoreMarkConfig.class);

    @Value("${rabbitmq.host}")
    private String messageQueueHostname;

    @Value("${examstack.answersheet.posturi}")
    private String answerSheetPostUri;

    @Value("${examstack.exampaper.geturi}")
    private String examPaperGetUri;

    @Bean
    QueueingConsumer queueingConsumer() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(messageQueueHostname);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Constants.ANSWERSHEET_DATA_QUEUE, true, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(Constants.ANSWERSHEET_DATA_QUEUE, true, consumer);
        return consumer;

    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        String propertyFilePath = System.getProperty("user.dir")+"/" + File.separator + "scoremaker.properties";
        propertyFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/" + File.separator + "scoremaker.properties";;
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        org.springframework.core.io.Resource[] properties = new FileSystemResource[]{new FileSystemResource(propertyFilePath)};
        propertySourcesPlaceholderConfigurer.setLocations(properties);
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    HashMap<String, EduPaper> examPapersMap() {
        return new HashMap<String, EduPaper>();
    }

    @Bean
    String answerSheetPostUri() {
        return answerSheetPostUri;
    }

    @Bean
    String examPaperGetUri() {
        return examPaperGetUri;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
