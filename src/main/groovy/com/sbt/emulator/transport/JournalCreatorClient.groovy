package com.sbt.emulator.transport

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.exception.JournalSendException
import com.sbt.emulator.model.Journal
import org.slf4j.LoggerFactory

/**
 * Отправляет сообщения с журналами
 */
class JournalCreatorClient {
    private static final LOGGER = LoggerFactory.getLogger(JournalCreatorClient.class)

    /**
     * Отправляет в необходиый канал
     */
    private MessageSender<JournalMessage> sender

    JournalCreatorClient(MessageSender<JournalMessage> sender) {
        this.sender = sender
    }

    /**
     * Отправляет данные в журнал
     * @param message журнал
     */
    void sendJournal(Journal message) {
        LOGGER.info("sending message [requestId = {}, createDate = {}]", message.requestId, message.createDate)
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sending message: {}", message)
        }
        try {
            sender.send(new Converter(message).convert())
            LOGGER.info("sent successfully message [requestId = {}, createDate = {}]", message.requestId, message.createDate)
        } catch (Exception exc) {
            LOGGER.error("error sending message {}", message, exc)
            throw new JournalSendException(exc)
        }
    }

    private static class Converter {
        private Journal journal

        Converter(Journal journal) {
            this.journal = journal
        }

        JournalMessage convert() {
            return [
                    header : [
                            "serviceId"     : journal.requestId,
                            "lastChangeDate": journal.createDate
                    ],
                    payload: journal.body
            ] as JournalMessage
        }
    }
}
