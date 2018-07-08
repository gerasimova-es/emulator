package com.sbt.emulator.app.client

import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.exception.JournalSendException
import com.sbt.emulator.model.Journal
import com.sbt.emulator.transport.MessageSender
import org.junit.Before
import org.junit.Test

import static org.mockito.Matchers.any
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.mock

class JournalCreatorClientTest {
    MessageSender<JournalMessage> sender
    JournalCreatorClient client

    @Before
    void init() {
        sender = mock(MessageSender.class)
        client = new JournalCreatorClient(sender)
    }

    @Test(expected = IllegalArgumentException)
    void sendNull() {
        client.send(null)
    }

    @Test(expected = IllegalArgumentException)
    void sendWithoutRequestId() {
        client.send(new Journal(createDate: new Date(), body: "body"))
    }

    @Test(expected = IllegalArgumentException)
    void sendWithoutCreateDate() {
        client.send(new Journal(requestId: "1", body: "body"))
    }

    @Test(expected = IllegalArgumentException)
    void sendWithoutBody() {
        client.send(new Journal(requestId: "1", createDate: new Date()))
    }

    @Test(expected = JournalSendException)
    void sendWithError() {
        doThrow(RuntimeException.class).when(sender)
                .send(any(JournalMessage.class))
        client.send(new Journal(requestId: "1", createDate: new Date(), body: "body"))
    }


}
