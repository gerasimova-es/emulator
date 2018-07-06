package com.sbt.emulator

import com.sbt.emulator.model.Journal

/**
 * Сохраняет данные в хранилище
 */
interface JournalService {
    /**
     * Сохраняет журнал
     * @param journal сохранямый журанл
     */
    void save(Journal journal)
}