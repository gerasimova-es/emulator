package com.sbt.emulator.transport

/**
 * Вычитвает несколько сообщений
 * @param < T >  тип сообщения
 */
interface BatchMessageReceiver<T> {
    /**
     * Получает сообщения для обработки
     * @param messages сообщения
     */
    void receive(List<T> messages)
}
