package com.sbt.emulator.exception

import groovy.transform.CompileStatic

@CompileStatic
class JournalSendException extends RuntimeException {
    JournalSendException(Throwable cause) {
        super("Ошибка отправки журнала", cause)
    }
}
