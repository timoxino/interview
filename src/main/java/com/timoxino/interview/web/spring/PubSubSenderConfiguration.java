package com.timoxino.interview.web.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import com.timoxino.interview.web.dto.CandidateQuestionsMessage;

@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "cloudrun", matchIfMissing = false)
public class PubSubSenderConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(PubSubReceiverConfiguration.class);

    @Bean
    public DirectChannel pubSubOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
        return new PubSubMessageHandler(pubsubTemplate, "compiled_questions_topic");
    }

    @MessagingGateway(defaultRequestChannel = "pubSubOutputChannel")
    public interface PubSubQuestionsGateway {
        void sendQuestionsToPubSub(CandidateQuestionsMessage message);
    }
}
