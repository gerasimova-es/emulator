package com.sbt.emulator.service

import com.sbt.emulator.dao.DataRepository
import com.sbt.emulator.model.Journal
import org.apache.commons.lang.Validate

class JournalServiceImpl implements JournalService {

    DataRepository journalDao

    JournalServiceImpl(DataRepository journalDao) {
        this.journalDao = journalDao
    }

    @Override
    Journal save(Journal journal) {
        Validate.notNull(journal)
        Validate.noNullElements([
                journal.requestId,
                journal.createDate,
                journal.body,])

        return journalDao.save(journal)
    }
}
