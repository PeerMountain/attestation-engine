package com.kyc3.oracle.api.flow

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.api.router.OracleRouter
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.springframework.stereotype.Service

@Service
class EncryptedMessageFlow(
  private val oracleRouter: OracleRouter,
  private val messageParser: MessageParser,
  private val messageDecryptService: EncryptionService,
  private val oracleAPIResponse: OracleAPIResponse,
  private val signatureVerificationService: SignatureVerificationService,
) {

  fun encryptedMessage(from: EntityBareJid, chat: Chat, message: Message.EncryptedMessage) {
    val decryptedMessage = messageDecryptService.decryptMessage(message)
    val signedMessage = messageParser.parseSignedMessage(String(decryptedMessage))

    doIfValid(from.asEntityBareJidString(), signedMessage) { signed ->
      oracleRouter.route(signed, chat)
    }
      ?.let { oracleAPIResponse.responseToClient(signedMessage.publicKey, chat, it) }
  }

  fun doIfValid(
    from: String,
    message: Message.SignedMessage,
    consumer: (Message.SignedMessage) -> GeneratedMessageV3?
  ) =
    signatureVerificationService.verify(from, message)
      ?: consumer(message)
}