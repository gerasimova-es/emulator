package com.sbt.emulator.transport.kafka

import com.sbt.emulator.transport.MessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate

/**
 * Отправляет сообщения в кафку
 * @param < T >  тип отправляемого сообщения
 */
class KafkaMessageSender<T> implements MessageSender<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageSender.class)

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate

    @Value('kafka.topic.outcome')
    private String topic

    @Override
    void send(T message) {
        LOGGER.info("sending message='{}' to topic='{}'", message, topic)
        kafkaTemplate.send(topic, message)
    }
}
