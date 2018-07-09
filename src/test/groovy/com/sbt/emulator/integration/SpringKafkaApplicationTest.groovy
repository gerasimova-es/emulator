package com.sbt.emulator.integration

import com.sbt.emulator.app.ReaderApplication
import com.sbt.emulator.app.client.JournalCreatorClient
import com.sbt.emulator.model.Journal
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReaderApplication.class)
class SpringKafkaApplicationTest {
    private static final String IN_TOPIC = "journal"
    private static final String FAILURE_TOPIC = "journal_failure"

    @ClassRule
    public static KafkaEmbedded embeddedKafka =
            new KafkaEmbedded(1, true, IN_TOPIC, FAILURE_TOPIC)

    @Autowired
    private JournalCreatorClient client

    //todo @before method with execute create-table.sql

    @Test
    void testSend() throws Exception {
        for (int i = 0; i < 300; i++) {
            client.send(
                    new Journal(
                            "createDate": new Date(),
                            "requestId": UUID.randomUUID().toString(),
                            "body": "I'm journal message 1!"))
        }
    }

    //todo test listener
}