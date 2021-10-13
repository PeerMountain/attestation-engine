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
    chatManager: ChatManager
) {

    private val jid: EntityBareJid = JidCreate.entityBareFrom("oracle-fe@jabber.hot-chilli.net")
    private val chat: Chat = chatManager.chatWith(jid)

    fun sendToFrontend(publicKey: String, message: GeneratedMessageV3) =
        oracleAPIResponse.responseToClient(chat, message)
}
