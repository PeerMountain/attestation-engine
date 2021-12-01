package com.kyc3.oracle.service

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.api.OracleAPIResponse
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping

@Service
class TimestampAPService(
    private val oracleAPIResponse: OracleAPIResponse,
    chatManager: ChatManager,
    private val userKeysService: UserKeysService,
    private val exchangeKeysHolder: ExchangeKeysHolder
) {

    private val jid: EntityBareJid = JidCreate.entityBareFrom("0x3f9dc0e46eb11ec9d91a6a2e1ec1916d5d0d2982@xmpp.kyc3.com")
    private val chat: Chat = chatManager.chatWith(jid)
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun getApPublicKey() {
        oracleAPIResponse.responseDirectly(
            chat,
            Message.GeneralMessage.newBuilder()
                .setExchange(exchangeKeysHolder.generateExchangeMessageRequest())
                .build()
        )
    }

    fun generateChallenge(userAddress: String, userPublicKey: String, nftType: Int): Unit =
        sendToProvider(
            GenerateChallenge.GenerateChallengeRequest.newBuilder()
                .setUserPublicKey(userPublicKey)
                .setUserAddress(userAddress)
                .setNftType(nftType)
                .build()
        )

    fun sendToProvider(message: GeneratedMessageV3): Unit =
        userKeysService.getUserKeys("0x3f9dc0e46eb11ec9d91a6a2e1ec1916d5d0d2982@xmpp.kyc3.com".lowercase())
            ?.let {
                oracleAPIResponse.responseToClient(chat, message)
            }
            ?: run {
                log.warn("AP public keys are not initialized")
                getApPublicKey()
            }
}
