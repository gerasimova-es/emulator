package com.sbt.emulator.transport

interface MessageReceiver<T> {
    void receive(List<T> messages)
}
