package com.timoxino.interview.web.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import com.google.common.collect.Multimap;
import com.timoxino.interview.web.annotation.GcpCloudRun;
import com.timoxino.interview.web.dto.CandidateExtractedSkillsMessage;
import com.timoxino.interview.web.dto.CandidateQuestionsMessage;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.service.QuestionLookerService;
import com.timoxino.interview.web.spring.PubSubSenderConfiguration.PubSubQuestionsGateway;

@Configuration
@GcpCloudRun
public class PubSubReceiverConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(PubSubReceiverConfiguration.class);

    @Autowired
    PubSubQuestionsGateway pubSubGateway;

    @Autowired
    QuestionLookerService questionLookerService;

    @Bean
    public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
        return new JacksonPubSubMessageConverter(objectMapper);
    }

    /**
     * Create a message channel for messages arriving from the subscription
     * `sage_subscription`.
     */
    @Bean
    public MessageChannel inputMessageChannel() {
        return new PublishSubscribeChannel();
    }

    /**
     * Create an inbound channel adapter to listen to the subscription
     * `sage_subscription` and send
     * messages to the input message channel.
     */
    @Bean
    public PubSubInboundChannelAdapter inboundChannelAdapter(
            @Qualifier("inputMessageChannel") MessageChannel messageChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "sage_subscription");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(CandidateExtractedSkillsMessage.class);
        return adapter;
    }

    /**
     * Define what happens to the messages arriving in the message channel.
     */
    @ServiceActivator(inputChannel = "inputMessageChannel")
    public void messageReceiver(
            CandidateExtractedSkillsMessage payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        LOGGER.warn("Message arrived from 'extracted_skills_topic'. Payload: " + payload.toString());

        Multimap<String, DataNode> questions = questionLookerService.prepareQuestions(payload.getRole(),
                payload.getLvlExpected());

        CandidateQuestionsMessage messageToSend = new CandidateQuestionsMessage();
        messageToSend.setQuestions(questions);
        messageToSend.setCvUri(payload.getCvUri());
        messageToSend.setLvlEstimated(payload.getLvlEstimated());
        messageToSend.setLvlExpected(payload.getLvlExpected());
        messageToSend.setRole(payload.getRole());

        pubSubGateway.sendQuestionsToPubSub(messageToSend);
        message.ack();
    }
}
