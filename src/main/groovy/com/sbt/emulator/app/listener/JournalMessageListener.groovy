package com.sbt.emulator.app.listener

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.model.Journal
import com.sbt.emulator.service.JournalService
import com.sbt.emulator.transport.MessageReceiver
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener

/**
 * Обрабатывает сообщения с журналами
 */
class JournalMessageListener implements MessageReceiver<JournalMessage> {
    static final Logger LOGGER = LoggerFactory.getLogger(JournalMessageListener.class)
    /**
     * Сервис для работы с журналами
     */
    final JournalService journalService

    JournalMessageListener(JournalService journalService) {
        this.journalService = journalService
    }

    @Override
    @KafkaListener(topics = '${kafka.topic.income}', containerFactory = "kafkaListenerContainerFactory")
    void receive(JournalMessage message) {
        Validate.notNull(message)
        try {
            //todo may be aspects instead of direct calling logger.debug
            LOGGER.info("handling message [requestId = {}]", message?.header?.requestId)
            Journal journal = journalService.save(
                    new Converter(message)
                            .validate()
                            .convert())
            LOGGER.info("message was handled successfully [requestId = {}, id = {}]", journal.requestId, journal.requestId)
        } catch (Exception exc) {
            LOGGER.error("saving journal error [message = {}]", message, exc)
        }
    }

    private static class Converter {
        JournalMessage message

        Converter(JournalMessage message) {
            this.message = message
        }

        Converter validate() {
            Validate.noNullElements([
                    message.header,
                    message.payload])
            Validate.noNullElements([
                    message.header.requestId,
                    message.header.createDate])
            return this
        }

        Journal convert() {
            new Journal(
                    requestId: message.header.requestId,
                    createDate: message.header.createDate,
                    body: message.payload
            )
        }
    }
}
