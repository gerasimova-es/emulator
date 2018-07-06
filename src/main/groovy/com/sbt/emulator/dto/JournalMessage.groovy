package com.sbt.emulator.dto

import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders

/**
 * Сообещние
 */
class JournalMessage implements Message<String> {
    /**
     * Заголовок
     */
    MessageHeaders header
    /**
     * Тело
     */
    String payload

    @Override
    String getPayload() {
        return payload
    }

    @Override
    MessageHeaders getHeaders() {
        return header
    }
}
