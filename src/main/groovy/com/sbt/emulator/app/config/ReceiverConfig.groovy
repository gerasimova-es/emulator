package com.sbt.emulator.app.config

import com.sbt.emulator.app.listener.JournalMessageListener
import com.sbt.emulator.dao.DataRepository
import com.sbt.emulator.dao.JournalRepository
import com.sbt.emulator.dto.JournalMessage
import com.sbt.emulator.model.Journal
import com.sbt.emulator.service.JournalService
import com.sbt.emulator.service.JournalServiceImpl
import com.sbt.emulator.transport.MessageReceiver
import groovy.transform.CompileStatic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.messaging.Message

@Configuration
@EnableKafka
@CompileStatic
class ReceiverConfig {

    @Value('${kafka.bootstrap-servers}')
    String bootstrapServers

    @Value('${kafka.group}')
    String groupId

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
        new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(JournalMessage.class))
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, JournalMessage>> kafkaListenerContainerFactory() {
        new ConcurrentKafkaListenerContainerFactory<>(
                consumerFactory: consumerFactory(), autoStartup: Boolean.TRUE)
    }

    @Bean
    DataRepository<Journal> journalRepository(JdbcOperations jdbcOperations) {
        new JournalRepository(jdbcOperations)
    }

    @Bean
    JournalService journalService(DataRepository<Journal> journalRepository) {
        new JournalServiceImpl(journalRepository)
    }

    @Bean
    MessageReceiver<JournalMessage> receiver(JournalService journalService) {
        new JournalMessageListener(journalService)
    }

    @Bean
    KafkaListenerErrorHandler voidSendToErrorHandler() {
        return new KafkaListenerErrorHandler() {
            @Override
            Object handleError(Message<?> message, ListenerExecutionFailedException exception) throws Exception {
                //todo logging some information about error and message
                return message //return result will be ignored...

            }
        }
    }
}
