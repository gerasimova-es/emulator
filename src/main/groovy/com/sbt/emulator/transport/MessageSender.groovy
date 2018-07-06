package com.sbt.emulator.transport

/**
 * Отправляет сообщение
 * @param < T >  тип сообщения
 */
interface MessageSender<T> {
    /**
     * Отправка сообщения
     * @param message сообщени
     */
    void send(T message)
}