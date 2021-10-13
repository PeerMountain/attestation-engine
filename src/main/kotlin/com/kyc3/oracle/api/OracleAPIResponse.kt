package com.kyc3.oracle.api

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.oracle.api.flow.EncryptionService
import com.kyc3.oracle.service.SignatureHelper
import com.kyc3.oracle.service.UserKeysService
import com.kyc3.oracle.service.Web3Service
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class OracleAPIResponse(
    private val base64Encoder: Base64.Encoder,
    private val web3Service: Web3Service,
    private val encryptionService: EncryptionService,
    private val userKeysService: UserKeysService,
) {

    fun responseDirectly(chat: Chat, message: GeneratedMessageV3) =
        chat.send(encodeMessage(message))

    fun responseToClient(chat: Chat, message: GeneratedMessageV3): Unit? =
        Any.pack(message)
            .let {
                Message.SignedAddressedMessage.newBuilder()
                    .setMessage(it)
                    .setSignature(SignatureHelper.toString(web3Service.sign(encodeMessage(it))))
                    .build()
            }
            .let {
                Message.SignedMessage.newBuilder()
                    .setAddressed(it)
                    .build()
            }
            .toByteArray()
            .let { base64Encoder.encodeToString(it) }
            .let {
                userKeysService.getUserKeys(
                    chat.xmppAddressOfChatPartner.asEntityBareJidString()
                )
                    ?.let { userKeys -> encryptionService.encryptMessage(userKeys.publicEncryptionKey, it) }
            }
            ?.let {
                Message.EncryptedMessage.newBuilder()
                    .setVersion(it.version)
                    .setNonce(it.nonce)
                    .setEphemPublicKey(it.ephemPublicKey)
                    .setCipherText(it.cipherText)
                    .build()
            }
            ?.let {
                Message.GeneralMessage.newBuilder()
                    .setMessage(it)
                    .build()
            }
            ?.toByteArray()
            ?.let { base64Encoder.encodeToString(it) }
            ?.let { chat.send(it) }

    private fun encodeMessage(message: GeneratedMessageV3): String =
        base64Encoder.encodeToString(message.toByteArray())
}
