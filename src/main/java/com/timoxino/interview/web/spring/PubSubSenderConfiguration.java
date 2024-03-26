package com.timoxino.interview.web.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import com.timoxino.interview.web.annotation.GcpCloudRun;
import com.timoxino.interview.web.dto.CandidateQuestionsMessage;

@Configuration
@GcpCloudRun
public class PubSubSenderConfiguration {

    @Bean
    public DirectChannel pubSubOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "pubSubOutputChannel")
    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
        return new PubSubMessageHandler(pubsubTemplate, "compiled_questions_topic");
    }

    @MessagingGateway(defaultRequestChannel = "pubSubOutputChannel")
    @GcpCloudRun
    public interface PubSubQuestionsGateway {
        void sendQuestionsToPubSub(CandidateQuestionsMessage message);
    }
}
