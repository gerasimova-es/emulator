package com.sbt.emulator.model

import groovy.transform.CompileStatic

@CompileStatic
class Journal {
    Long id
    String requestId
    Date createDate
    String body

    @Override
    String toString() {
        return "Journal{" +
                "id=" + id +
                ", requestId='" + requestId + '\'' +
                ", createDate=" + createDate +
                ", body='" + body + '\'' +
                '}';
    }
}
