package com.kyc3.oracle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Configuration
class Web3JConfig {

  @Bean
  fun web3j(): Web3j =
      Web3j.build(HttpService("http://localhost:8545"))
}