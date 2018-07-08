package com.sbt.emulator.transport.kafka

import com.sbt.emulator.transport.MessageSender
import groovy.transform.CompileStatic
import org.springframework.kafka.core.KafkaTemplate
/**
 * Отправляет сообщения в кафку
 * @param < T >  тип отправляемого сообщения
 */
@CompileStatic
class KafkaMessageSender<T> implements MessageSender<T> {
    /**
     * Топик, в который будут отправляться сообщения
     */
    String topic
    /**
     * темплейт для взаимодействия с message producer
     */
    KafkaTemplate<String, T> kafkaTemplate

    KafkaMessageSender(KafkaTemplate<String, T> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate
        this.topic = topic
    }

    @Override
    void send(T message) {
        kafkaTemplate.send(topic, message)
        kafkaTemplate.flush()
    }
}
