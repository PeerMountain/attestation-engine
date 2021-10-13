package com.kyc3.oracle.config

import com.kyc3.oracle.config.properties.XmppProperties
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.c2s.ModularXmppClientToServerConnection
import org.jivesoftware.smack.c2s.ModularXmppClientToServerConnectionConfiguration
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.websocket.XmppWebSocketTransportModuleDescriptor
import org.jivesoftware.smackx.admin.ServiceAdministrationManager
import org.jivesoftware.smackx.iqregister.AccountManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class XMPPConfiguration(
    val xmppProperties: XmppProperties
) {

    @Bean
    fun connectionConfiguration(): ModularXmppClientToServerConnectionConfiguration =
        ModularXmppClientToServerConnectionConfiguration.builder()
            .also {
                it.removeAllModules()
            }
            .setXmppDomain(xmppProperties.domain)
            .setUsernameAndPassword(xmppProperties.user.userName, xmppProperties.user.password)
            .setHost(xmppProperties.host)
            .also {
                it.addModule(
                    XmppWebSocketTransportModuleDescriptor.getBuilder(
                        it
                    )
                        .explicitlySetWebSocketEndpoint("wss://xmpp.kyc3.com/xmpp-websocket")
                        .build()
                )
            }
            .build()

    @Bean
    fun adminConnectionConfiguration(): ModularXmppClientToServerConnectionConfiguration =
        ModularXmppClientToServerConnectionConfiguration.builder()
            .also {
                it.removeAllModules()
            }
            .setXmppDomain(xmppProperties.domain)
            .setUsernameAndPassword(xmppProperties.admin.userName, xmppProperties.admin.password)
            .setHost(xmppProperties.host)
            .also {
                it.addModule(
                    XmppWebSocketTransportModuleDescriptor.getBuilder(
                        it
                    )
                        .explicitlySetWebSocketEndpoint("wss://xmpp.kyc3.com/xmpp-websocket")
                        .build()
                )
            }
            .build()


    @Bean
    fun connection(
        connectionConfiguration: ModularXmppClientToServerConnectionConfiguration
    ): XMPPConnection =
        ModularXmppClientToServerConnection(connectionConfiguration)
            .also {
                it.connect()
                it.login()
            }

    @Bean
    fun adminConnection(
        adminConnectionConfiguration: ModularXmppClientToServerConnectionConfiguration
    ): XMPPConnection =
        ModularXmppClientToServerConnection(adminConnectionConfiguration)
            .also {
                it.connect()
                it.login()
            }

    @Bean
    fun chatManager(connection: XMPPConnection): ChatManager =
        ChatManager.getInstanceFor(connection)

    @Bean
    fun serviceAdministrationManager(
        adminConnection: XMPPConnection
    ): ServiceAdministrationManager =
        ServiceAdministrationManager(adminConnection)
}
