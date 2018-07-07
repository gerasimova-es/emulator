package com.sbt.emulator.app.config

import com.sbt.emulator.app.listener.JournalMessageListener
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.service.JournalService
import com.sbt.emulator.service.JournalServiceImpl
import com.sbt.emulator.transport.MessageReceiver
import groovy.transform.CompileStatic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableKafka
@CompileStatic
class ReceiverContext {

    @Value('${kafka.bootstrap-servers}')
    private String bootstrapServers

    @Value('${kafka.group}')
    private String groupId

    @Bean
    Map<String, Object> consumerConfigs() {
        def config = [:]
        //todo set bootstrap servers and group id from standin-service configuration (PlatformPropertyLoader)
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        //todo use jaxb serializer like journal creator client library
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
//        config.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 128)

        return config
    }

    @Bean
    ConsumerFactory<String, JournalMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(JournalMessage.class))
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, JournalMessage>> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>(
                consumerFactory: consumerFactory(),
                batchListener: Boolean.FALSE, autoStartup: Boolean.TRUE)
    }

    @Bean
    JournalService journalService() {
        return new JournalServiceImpl()
    }

    @Bean
    MessageReceiver<JournalMessage> receiver() {
        return new JournalMessageListener(journalService())
    }
}
