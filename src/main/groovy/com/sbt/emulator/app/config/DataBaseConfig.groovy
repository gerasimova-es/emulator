package com.sbt.emulator.app.config

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
@CompileStatic
class DataBaseConfig {

    @Value('${jdbc.driver}')
    String driver
    @Value('${jdbc.url}')
    String url

    @Bean
    DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(driver)
        dataSourceBuilder.url(url)
        return dataSourceBuilder.build()
    }
}
