package com.sbt.emulator.model

import groovy.transform.CompileStatic

@CompileStatic
class Journal {
    Long id
    String requestId
    Date createDate
    String body

    Journal() {
    }

    Journal(Journal source) {
        this.id = source.id
        this.requestId = source.requestId
        this.createDate = source.createDate
        this.body = source.body
    }

    @Override
    String toString() {
        return """Journal{id=${id}, requestId='${requestId}', createDate=${createDate}, body='${body}'}"""
    }

}
