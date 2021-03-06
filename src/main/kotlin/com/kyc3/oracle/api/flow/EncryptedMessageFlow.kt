package com.kyc3.oracle.api.flow

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.oracle.api.APIResponseService
import com.kyc3.oracle.api.EncryptionService
import com.kyc3.oracle.api.router.OracleRouter
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class EncryptedMessageFlow(
    private val oracleRouter: OracleRouter,
    private val messageParser: MessageParser,
    private val messageEncryptionService: EncryptionService,
    private val apiResponse: APIResponseService,
    private val signatureVerificationService: SignatureVerificationService,
) {

    fun encryptedMessage(from: EntityBareJid, chat: Chat, message: Message.EncryptedMessage) {
        val decryptedMessage = messageEncryptionService.decryptMessage(message)
        val signedMessage = messageParser.parseSignedMessage(String(decryptedMessage))

        doIfValid(from.asEntityBareJidString(), signedMessage) { signed ->
            oracleRouter.route(signed, chat)
        }
            .let { future ->
                future.thenAccept {
                    it?.also {
                        apiResponse.responseToClient(chat, it)
                    }
                }
            }
    }

    private fun doIfValid(
        from: String,
        message: Message.SignedMessage,
        consumer: (Message.SignedMessage) -> CompletableFuture<out GeneratedMessageV3?>
    ): CompletableFuture<out GeneratedMessageV3?> =
        signatureVerificationService.verify(from, message)
            .thenCompose {
                if (it != null) {
                    val completedFuture: CompletableFuture<out GeneratedMessageV3?> =
                        CompletableFuture.completedFuture(it)
                    completedFuture
                } else {
                    consumer(message)
                }
            }
}
