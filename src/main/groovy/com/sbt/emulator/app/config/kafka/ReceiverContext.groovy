package com.sbt.emulator.app.config.kafka;

import com.sbt.emulator.dto.JournalMessage;
import com.sbt.emulator.transport.MessageReceiver
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableKafka
class ReceiverContext {
    private static final String RECEIVE_TYPE = "batch"

    @Value('${kafka.bootstrap-servers}')
    private String bootstrapServers

    @Value('${kafka.max-poll-size}')
    private Integer maxPollSize

    @Bean
    Map<String, Object> consumerConfigs() {
        def config = [:]
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
        config.put(ConsumerConfig.GROUP_ID_CONFIG, RECEIVE_TYPE)
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollSize)
        return config
    }

    @Bean
    ConsumerFactory<String, JournalMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs())
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, JournalMessage>> listenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>(
                consumerFactory: consumerFactory(), batchListener: true)
    }

    @Bean
    MessageReceiver<JournalMessage> receiver() {
        return new JournalKafkaMessageReceiver()
    }
}
