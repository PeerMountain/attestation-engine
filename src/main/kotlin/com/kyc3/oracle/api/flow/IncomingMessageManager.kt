package com.kyc3.oracle.api.flow

import com.kyc3.Message
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.springframework.stereotype.Service

@Service
class IncomingMessageManager(
    private val messageParser: MessageParser,
    private val encryptedMessageFlow: EncryptedMessageFlow,
    private val exchangeMessageFlow: ExchangeMessageFlow
) {

    fun incomingMessage(from: EntityBareJid, chat: Chat, body: String) {
        val generalMessage = messageParser.parseGeneralMessage(body)

        when (generalMessage.bodyCase) {
            Message.GeneralMessage.BodyCase.EXCHANGE ->
                exchangeMessageFlow.exchange(from, chat, generalMessage.exchange)
            Message.GeneralMessage.BodyCase.MESSAGE ->
                encryptedMessageFlow.encryptedMessage(from, chat, generalMessage.message)
            Message.GeneralMessage.BodyCase.BODY_NOT_SET -> throw IllegalStateException("Body not set")
        }
    }
}
