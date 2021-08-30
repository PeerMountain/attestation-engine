package com.kyc3.oracle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "libsodium")
class LibsodiumProperties {
  lateinit var path: String
}