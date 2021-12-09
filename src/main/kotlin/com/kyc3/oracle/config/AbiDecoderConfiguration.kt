package com.kyc3.oracle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.abi.DefaultFunctionReturnDecoder

@Configuration
class AbiDecoderConfiguration {

    @Bean
    fun functionDecoder(): DefaultFunctionReturnDecoder = DefaultFunctionReturnDecoder()
}
