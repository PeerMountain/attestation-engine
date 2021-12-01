package com.kyc3.oracle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("contracts")
class ContractsProperties {
    lateinit var cashier: String
    lateinit var trustToken: String
    lateinit var erc20: String
}
