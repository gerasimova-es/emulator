package com.sbt.emulator.service

import com.sbt.emulator.dao.DataRepository
import com.sbt.emulator.model.Journal
import groovy.transform.CompileStatic
import org.apache.commons.lang.Validate

@CompileStatic
class JournalServiceImpl implements JournalService {

    DataRepository<Journal> journalDao

    JournalServiceImpl(DataRepository<Journal> journalDao) {
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
