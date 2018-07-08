package com.sbt.emulator.dao

/**
 * Работа с хранилищем данных
 * @param < T >  тип обекта
 */
interface DataRepository<T> {
    /**
     * сохранение объект
     * @param object сохраняемый объекь
     */
    T save(T object)
}