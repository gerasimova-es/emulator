package com.sbt.emulator.mock

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
