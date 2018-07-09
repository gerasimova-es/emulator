package com.sbt.emulator.exception

import groovy.transform.CompileStatic

@CompileStatic
class JournalSaveException extends RuntimeException {
    JournalSaveException(Throwable cause) {
        super("journal was not saved", cause)
    }
}
