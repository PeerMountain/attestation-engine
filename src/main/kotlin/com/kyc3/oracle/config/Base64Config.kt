package com.kyc3.oracle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class Base64Config {

  @Bean
  fun base64Decoder(): Base64.Decoder = Base64.getDecoder()

  @Bean
  fun base64Encoder(): Base64.Encoder = Base64.getEncoder()
}