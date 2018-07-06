package com.sbt.emulator.transport

interface MessageSender<T> {
    void send(T message)
}