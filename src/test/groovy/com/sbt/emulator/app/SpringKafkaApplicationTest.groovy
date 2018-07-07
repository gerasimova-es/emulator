package com.sbt.emulator.app

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
@SpringBootTest
class SpringKafkaApplicationTest {
    private static final String IN_TOPIC = "journal.t"

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, IN_TOPIC)

    @Autowired
    private JournalCreatorClient client

    @Test
    void testSend() throws Exception {
        for (int i = 0; i < 250; i++) {
            client.send(
                    new Journal(
                            "createDate": new Date(),
                            "requestId": UUID.randomUUID().toString(),
                            "body": "I'm journal message 1!"))
        }
        Thread.sleep(10000)
    }
}