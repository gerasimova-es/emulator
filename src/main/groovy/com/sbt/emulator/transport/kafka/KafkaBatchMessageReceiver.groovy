package com.sbt.emulator.transport.kafka

import com.sbt.emulator.transport.BatchMessageReceiver
import org.springframework.kafka.annotation.KafkaListener
/**
 * Обрабатывает сообщения от прикладного модуля
 */
abstract class KafkaBatchMessageReceiver<T> implements BatchMessageReceiver<T> {

    @Override
    @KafkaListener(topics = '${kafka.topic.income}')
    abstract void receive(List<T> messages)
}
