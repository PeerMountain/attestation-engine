package com.kyc3.oracle.service

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.api.APIResponseService
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping

@Service
class TimestampAPService(
    private val apiResponse: APIResponseService,
    chatManager: ChatManager,
    private val userKeysService: UserKeysService,
    private val exchangeKeysHolder: ExchangeKeysHolder
) {

    private val jid: EntityBareJid = JidCreate.entityBareFrom("0x80410613b808bf416acc81a677bf8b7da800c842@xmpp.kyc3.com")
    private val chat: Chat = chatManager.chatWith(jid)
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun getApPublicKey() {
        apiResponse.responseDirectly(
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
        userKeysService.getUserKeys("0x80410613b808bf416acc81a677bf8b7da800c842@xmpp.kyc3.com".lowercase())
            ?.let {
                apiResponse.responseToClient(chat, message)
            }
            ?: run {
                log.warn("AP public keys are not initialized")
                getApPublicKey()
            }
}
