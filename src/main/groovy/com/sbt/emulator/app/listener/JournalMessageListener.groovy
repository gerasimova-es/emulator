package com.sbt.emulator.app.listener

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.model.Journal
import com.sbt.emulator.service.JournalService
import com.sbt.emulator.transport.MessageReceiver
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.SendTo
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
    @KafkaListener(
            id = "journalListener",
            topics = '${kafka.topic.income}',
            errorHandler = "voidSendToErrorHandler",
            containerFactory = "kafkaListenerContainerFactory")
    @SendTo('${kafka.topic.failure}')
    void receive(JournalMessage message) {
        Validate.notNull(message)

        //todo maybe to use aspects instead of direct calling logger
        LOGGER.debug("handling message [requestId = {}]", message.header?.requestId)
        Journal journal = journalService.save(
                new Converter(message)
                        .validate()
                        .convert())
        LOGGER.debug("message was handled successfully [requestId = {}, id = {}]", journal.requestId, journal.requestId)
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
