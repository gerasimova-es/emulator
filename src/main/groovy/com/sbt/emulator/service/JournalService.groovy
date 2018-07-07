package com.sbt.emulator.service

import com.sbt.emulator.model.Journal

/**
 * Сохраняет данные в хранилище
 */
interface JournalService {
    /**
     * Сохраняет журнал в хранилище
     * @param journal сохранямый журнал
     */
    Journal save(Journal journal)
}