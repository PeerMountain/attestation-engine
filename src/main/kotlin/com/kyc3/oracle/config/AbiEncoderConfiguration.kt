package com.kyc3.oracle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.abi.DefaultFunctionEncoder

@Configuration
class AbiEncoderConfiguration {

    @Bean
    fun functionEncoder(): DefaultFunctionEncoder = DefaultFunctionEncoder()
}
