package com.sbt.emulator.transport.kafka

import com.sbt.emulator.transport.MessageSender
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
/**
 * Отправляет сообщения в кафку
 * @param < T >  тип отправляемого сообщения
 */
@CompileStatic
class KafkaMessageSender<T> implements MessageSender<T> {

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate

    @Value('${kafka.topic.outcome}')
    private String topic

    @Override
    void send(T message) {
        kafkaTemplate.send(topic, message)
        kafkaTemplate.flush()
    }
}
