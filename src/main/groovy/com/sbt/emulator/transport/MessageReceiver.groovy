package com.sbt.emulator.transport

/**
 * Вычитвает сообщение
 * @param < T >    тип сообщения
 */
interface MessageReceiver<T> {
    /**
     * Получает сообщение для обработки
     * @param message сообщение
     */
    void receive(T message)
}
