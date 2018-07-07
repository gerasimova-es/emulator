package com.sbt.emulator.dto

import groovy.transform.CompileStatic

/**
 * Сообещние
 */
@CompileStatic
class JournalMessage {
    /**
     * Заголовок
     */
    JournalHeader header
    /**
     * Тело
     */
    String payload

    String getPayload() {
        return payload
    }

    JournalHeader getHeader() {
        return header
    }


    @Override
    String toString() {
        return "JournalMessage{" +
                "header=" + header?.toString() +
                ", payload='" + payload + '\'' +
                '}'
    }
}
