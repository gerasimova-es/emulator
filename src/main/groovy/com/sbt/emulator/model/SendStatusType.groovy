package com.sbt.emulator.model

/**
 * Статус обработки сообщения
 */
enum SendStatusType {
    /**
     * Не обработано
     */
    NOT_HANDLED,
    /**
     * Успешно обработано
     */
    SUCCESS,
    /**
     * Ошибка обработки
     */
    ERROR
}