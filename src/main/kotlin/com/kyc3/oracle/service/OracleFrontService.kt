package com.kyc3.oracle.service

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.oracle.api.APIResponseService
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.impl.JidCreate
import org.springframework.stereotype.Component

@Component
class OracleFrontService(
    private val apiResponse: APIResponseService,
    private val chatManager: ChatManager
) {

    fun sendToFrontend(
        userAddress: String,
        publicKey: String,
        message: GeneratedMessageV3
    ) =
        JidCreate.entityBareFrom("$userAddress@xmpp.kyc3.com")
            .let { chatManager.chatWith(it) }
            .let { apiResponse.responseToClient(it, message) }
}
