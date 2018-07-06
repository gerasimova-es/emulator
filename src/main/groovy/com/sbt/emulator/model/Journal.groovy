package com.sbt.emulator.model

class Journal {
    String requestId
    Date createDate
    String body

    @Override
    String toString() {
        return "Journal{" +
                "requestId='" + requestId + '\'' +
                ", createDate=" + createDate +
                ", body='" + body + '\'' +
                '}'
    }
}
