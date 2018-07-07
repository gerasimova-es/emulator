package com.sbt.emulator.app.listener

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.model.Journal
import com.sbt.emulator.service.JournalService
import com.sbt.emulator.transport.kafka.KafkaMessageReceiver
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Обрабатывает сообщения с журналами
 */
class JournalMessageListener extends KafkaMessageReceiver<JournalMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalMessageListener.class)
    /**
     * Сервис для работы с журналами
     */
    private final JournalService journalService

    @Autowired
    JournalMessageListener(JournalService journalService) {
        this.journalService = journalService
    }

    @Override
    void receive(JournalMessage message) {
        Validate.notNull(message)
        try {
            LOGGER.debug("handling message [requestId = {}]", message?.header?.requestId)
            Journal journal = journalService.save(
                    new Converter(message)
                            .validate()
                            .convert())
            LOGGER.debug("message was handled successfully [requestId = {}, journal id = {}]", journal.requestId, journal.id)
        } catch (Exception exc) {
            LOGGER.error("saving journal error [message = {}]", message)
        }
    }

    private static class Converter {
        private JournalMessage message

        Converter(JournalMessage message) {
            this.message = message
        }

        Converter validate() {
            Validate.notNull(message.header, "header cannot be null")
            Validate.notNull(message.header.requestId, "header.requestId cannot be null")
            Validate.notNull(message.header.createDate, "header.createDate cannot be null")
            Validate.notNull(message.payload, "header.payload cannot be null")
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
