package com.sbt.emulator.exception

class JournalSaveException extends RuntimeException {
    JournalSaveException(Throwable cause) {
        super("journal was not saved", cause)
    }
}
