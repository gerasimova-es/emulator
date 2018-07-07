package com.sbt.emulator.app

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@CompileStatic
class ReaderApplication {
    static void main(String[] args) {
        SpringApplication.run(ReaderApplication.class, args)
    }
}
