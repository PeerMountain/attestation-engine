package com.kyc3.oracle.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "xmpp")
class XmppProperties {
    lateinit var domain: String
    lateinit var host: String
    var user: Credentials = Credentials()
    var admin: Credentials = Credentials()

    class Credentials {
        lateinit var userName: String
        lateinit var password: String
    }
}
