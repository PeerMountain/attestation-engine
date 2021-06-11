package com.kyc3.oracle.config

import com.kyc3.oracle.config.properties.Web3JProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Configuration
class Web3JConfig(
  private val web3JProperties: Web3JProperties
) {

  @Bean
  fun web3j(): Web3j =
    Web3j.build(HttpService(web3JProperties.host))
}