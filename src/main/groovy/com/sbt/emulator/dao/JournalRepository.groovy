package com.sbt.emulator.dao

import com.sbt.emulator.exception.JournalSaveException
import com.sbt.emulator.model.Journal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class JournalRepository implements DataRepository<Journal> {
    static final Logger LOGGER = LoggerFactory.getLogger(JournalRepository.class)

    final static String INSERT =
            """INSERT INTO journal(request_id, create_date, body)                   
               VALUES (?, ?, ?);"""

    private JdbcOperations jdbcOperations

    JournalRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations
    }

    @Override
    Journal save(Journal journal) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder()
            jdbcOperations.update(
                    new PreparedStatementCreator() {
                        @Override
                        PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement statement =
                                    connection.prepareStatement(INSERT, ["id"] as String[])
                            statement.setString(1, journal.requestId)
                            statement.setDate(2, new java.sql.Date(journal.createDate.time))
                            statement.setString(3, journal.body)
                            return statement
                        }
                    },
                    keyHolder)
            Journal saved = new Journal(journal)
            saved.id = keyHolder.key.longValue()
            return saved

        } catch (SQLException exc) {
            throw new JournalSaveException(exc)
        }
    }
}
