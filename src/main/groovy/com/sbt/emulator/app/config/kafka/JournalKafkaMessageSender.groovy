package com.sbt.emulator.app.config.kafka

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.transport.MessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate

class JournalKafkaMessageSender implements MessageSender<JournalMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalKafkaMessageSender.class)

    @Autowired
    private KafkaTemplate<String, JournalMessage> kafkaTemplate

    @Value('kafka.topic.outcome')
    private String topic

    void send(JournalMessage message) {
        LOGGER.info("sending message='{}' to topic='{}'", message, topic)
        kafkaTemplate.send(topic, message)
    }
}
