package com.sbt.emulator.transport

import com.sbt.emulator.JournalService
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.model.Journal
import com.sbt.emulator.transport.kafka.KafkaBatchMessageReceiver
import org.slf4j.LoggerFactory

/**
 * Обрабатывает сообщения с журналами
 */
class JournalMessageListener extends KafkaBatchMessageReceiver<JournalMessage> {
    private static final LOGGER = LoggerFactory.getLogger(JournalMessageListener.class)
    /**
     * Сервис для работы с журналами
     */
    private JournalService journalService

    @Override
    void receive(List<JournalMessage> messages) {
        LOGGER.info("received messages [size = {}]", messages.size())
        messages.stream()
                .peek { message -> LOGGER.debug("handling message [requestId = {}]", message.header."requestId") }
                .map { message -> new Converter(message).convert() }
                .forEach { journal ->
            try {
                journalService.save(journal)
                LOGGER.debug("journal has handled successfully [requestId = {}, createDate = {}]")
            } catch (Exception exc) {
                LOGGER.error("saving journal error [requestId = {}, createDate = {}]",
                        journal.requestId, journal.createDate, exc)
            }
        }
    }

    private static class Converter {
        private JournalMessage message

        Converter(JournalMessage message) {
            this.message = message
        }

        Journal convert() {
            return [
                    requestId : message.header."requestId",
                    createDate: message.header."createDate",
                    body      : message.header."body"
            ] as Journal
        }

    }
}
