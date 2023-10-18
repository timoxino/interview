package com.timoxino.interview.web.spring;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;

@Configuration
public class GenericConfiguration {
    @Bean
    public HttpExchangeRepository httpTraceRepository()
    {
        return new InMemoryHttpExchangeRepository();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        return objectMapper;
    }

    @Bean
    public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
        return new JacksonPubSubMessageConverter(objectMapper);
    }
}
