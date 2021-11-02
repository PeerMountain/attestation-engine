package com.kyc3.oracle.service

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.oracle.api.OracleAPIResponse
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.springframework.stereotype.Component

@Component
class OracleFrontService(
    private val oracleAPIResponse: OracleAPIResponse,
    private val chatManager: ChatManager
) {

    fun sendToFrontend(
        userAddress: String,
        publicKey: String,
        message: GeneratedMessageV3
    ) =
        JidCreate.entityBareFrom("$userAddress@xmpp.kyc3.com")
            .let { chatManager.chatWith(it) }
            .let { oracleAPIResponse.responseToClient(it, message) }
}
