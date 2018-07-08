package com.sbt.emulator.app.client

import com.sbt.emulator.dto.JournalHeader
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.exception.JournalSendException
import com.sbt.emulator.model.Journal
import com.sbt.emulator.transport.MessageSender
import groovy.transform.CompileStatic
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Отправляет сообщения с журналами
 */
@CompileStatic
class JournalCreatorClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalCreatorClient.class)
    /**
     * Транспорт для отправки сообщений
     */
    MessageSender<JournalMessage> sender

    JournalCreatorClient(MessageSender<JournalMessage> sender) {
        this.sender = sender
    }

    /**
     * Отправляет данные в журнал
     * @param journal журнал
     */
    void send(Journal journal) {
        Validate.notNull(journal)
        Validate.noNullElements([
                journal.requestId,
                journal.createDate,
                journal.body])
        try {
            LOGGER.debug("sending journal [requestId = {}, createDate = {}]", journal.requestId, journal.createDate)
            sender.send(
                    new Converter(journal).convert())
            LOGGER.debug("sent successfully journal [requestId = {}, createDate = {}]", journal.requestId, journal.createDate)
        } catch (Exception exc) {
            LOGGER.error("error journal message {}", journal, exc)
            throw new JournalSendException(exc)
        }
    }

    private static class Converter {
        Journal journal

        Converter(Journal journal) {
            this.journal = journal
        }

        JournalMessage convert() {
            new JournalMessage(
                    header: new JournalHeader(
                            requestId: this.journal.requestId,
                            createDate: this.journal.createDate
                    ),
                    payload: this.journal.body
            )
        }
    }
}
