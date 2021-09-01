package com.kyc3.oracle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "wallet")
class WalletProperties {
    lateinit var privateKey: String
}
