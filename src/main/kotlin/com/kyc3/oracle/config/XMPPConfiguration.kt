package com.kyc3.oracle.config

import com.kyc3.oracle.config.properties.XmppProperties
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class XMPPConfiguration(
    val xmppProperties: XmppProperties
) {

  @Bean
  fun xmppTcpConnectionConfiguration(): XMPPTCPConnectionConfiguration =
      XMPPTCPConnectionConfiguration.builder()
          .setXmppDomain(xmppProperties.domain)
          .setUsernameAndPassword(xmppProperties.userName, xmppProperties.password)
          .setHost(xmppProperties.host)
          .build()

  @Bean
  fun xmppTcpConnection(xmpptcpConnectionConfiguration: XMPPTCPConnectionConfiguration): XMPPTCPConnection =
      XMPPTCPConnection(xmpptcpConnectionConfiguration)
          .also {
            it.connect()
            it.login()
          }

  @Bean
  fun chatManager(xmpptcpConnection: XMPPTCPConnection): ChatManager =
      ChatManager.getInstanceFor(xmpptcpConnection)
}