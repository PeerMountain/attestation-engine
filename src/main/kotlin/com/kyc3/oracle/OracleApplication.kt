package com.kyc3.oracle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class OracleApplication

fun main(args: Array<String>) {
    runApplication<OracleApplication>(*args)
}
