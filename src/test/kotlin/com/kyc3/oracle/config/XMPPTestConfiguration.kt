package com.kyc3.oracle.config

import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smackx.admin.ServiceAdministrationManager
import org.jxmpp.jid.EntityBareJid
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class XMPPTestConfiguration {

    @Bean
    @Primary
    fun testChatManager(): ChatManager = Mockito.mock(ChatManager::class.java)
        .also {
            Mockito.`when`(it.chatWith(Mockito.any(EntityBareJid::class.java)))
                .thenReturn(Mockito.mock(Chat::class.java))
        }

    @Bean
    @Primary
    fun testServiceAdministrationManager(): ServiceAdministrationManager =
        Mockito.mock(ServiceAdministrationManager::class.java)
}
