package com.sbt.emulator.app.config

import com.sbt.emulator.app.client.JournalCreatorClient
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.transport.MessageSender
import com.sbt.emulator.transport.kafka.KafkaMessageSender
import groovy.transform.CompileStatic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@CompileStatic
class SenderConfig {

    @Value('${kafka.bootstrap-servers}')
    String bootstrapServers

    @Value('${kafka.topic.outcome}')
    String topic

    @Bean
    Map<String, Object> producerConfigs() {
        def config = [:]
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
//        config.put(ProducerConfig.SEND_BUFFER_CONFIG, 128)
        //todo set send buffer size
        return config
    }

    @Bean
    ProducerFactory<String, JournalMessage> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs())
    }

    @Bean
    KafkaTemplate<String, JournalMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory())
    }

    @Bean
    MessageSender<JournalMessage> sender() {
        return new KafkaMessageSender<JournalMessage>(kafkaTemplate(), topic)
    }

    @Bean
    JournalCreatorClient creatorClient() {
        return new JournalCreatorClient(sender())
    }
}
