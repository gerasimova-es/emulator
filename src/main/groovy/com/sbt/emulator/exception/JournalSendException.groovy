package com.sbt.emulator.exception

class JournalSendException extends RuntimeException {
    JournalSendException(Throwable cause) {
        super("Ошибка отправки журнала", cause)
    }
}
