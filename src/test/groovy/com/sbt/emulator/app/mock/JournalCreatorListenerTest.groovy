package com.sbt.emulator.app.mock

import com.sbt.emulator.dto.JournalHeader
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.mock.JournalMessageListener
import com.sbt.emulator.model.Journal
import com.sbt.emulator.service.JournalService
import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.fail
import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

@CompileStatic
class JournalCreatorListenerTest {
    private JournalService service
    private JournalMessageListener listener

    @Before
    void init() {
        service = mock(JournalService)
        listener = new JournalMessageListener(service)
    }

    @Test
    void receiveNull() {
        try {
            listener.receive(null)
            fail("expected IllegalArgumentException")
        } catch (IllegalArgumentException ignored) {
            verify(service, never()).save(any(Journal.class))
        }
    }

    @Test
    void receiveOneWithEmptyHeader() {
        listener.receive(
                new JournalMessage(
                        payload: "journal body 1"
                )
        )
        verify(service, never()).save(any(Journal.class))
    }

    @Test
    void receiveOne() {
        when(service.save(any(Journal.class))).thenReturn(new Journal())
        Date current = new Date()
        listener.receive(
                new JournalMessage(
                        header: new JournalHeader(
                                requestId: "1",
                                createDate: current),
                        payload: "journal body 1"
                )
        )
        verify(service, times(1)).save(any(Journal.class))
    }

    @Test
    void receiveWithError() {
        when(service.save(any(Journal.class)))
                .thenThrow(IllegalArgumentException.class)

        Date current = new Date()
        listener.receive(
                new JournalMessage(
                        header: new JournalHeader(
                                requestId: "1",
                                createDate: current),
                        payload: "journal body 1"
                )
        )
        verify(service, times(1)).save(any(Journal.class))
    }
}
