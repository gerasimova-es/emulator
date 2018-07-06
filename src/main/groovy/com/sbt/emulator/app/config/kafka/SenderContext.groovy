package com.sbt.emulator.app.config.kafka


import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.transport.MessageSender
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
class SenderContext {

    @Value('${kafka.bootstrap-servers}')
    private String bootstrapServers

    @Bean
    Map<String, Object> producerConfigs() {
        def config = [:]
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
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
        return new JournalKafkaMessageSender()
    }
}
