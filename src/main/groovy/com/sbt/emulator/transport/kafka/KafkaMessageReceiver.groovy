package com.sbt.emulator.transport.kafka

import com.sbt.emulator.transport.MessageReceiver
import groovy.transform.CompileStatic
import org.springframework.kafka.annotation.KafkaListener
/**
 * Обрабатывает сообщение от прикладного модуля
 */
@CompileStatic
abstract class KafkaMessageReceiver<T> implements MessageReceiver<T> {

    @Override
    @KafkaListener(topics = '${kafka.topic.income}', containerFactory = "kafkaListenerContainerFactory")
    abstract void receive(T messages)
}
