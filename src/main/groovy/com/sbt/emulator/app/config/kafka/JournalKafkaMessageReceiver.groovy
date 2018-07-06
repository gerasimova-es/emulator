package com.sbt.emulator.app.config.kafka


import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.transport.MessageReceiver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener

/**
 * Обрабатывает сообщения от прикладного модуля
 */
class JournalKafkaMessageReceiver implements MessageReceiver<JournalMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalKafkaMessageReceiver.class)

    @KafkaListener(topics = '${kafka.topic.income}')
    void receive(List<JournalMessage> messages) {
        LOGGER.info("received messages, size {}", messages.size())
    }

}
