package com.kyc3.oracle.api.flow

import com.google.protobuf.Any
import com.kyc3.ErrorDtoOuterClass
import com.kyc3.Message
import com.kyc3.oracle.service.Web3Service
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class SignatureVerificationService(
    private val web3Service: Web3Service,
    private val encoder: Base64.Encoder
) {

    fun verify(from: String, message: Message.SignedMessage): ErrorDtoOuterClass.ErrorDto? =
        when (message.bodyCase) {
            Message.SignedMessage.BodyCase.ANONYMOUS -> verifyAnonymous(message.anonymous)
            Message.SignedMessage.BodyCase.ADDRESSED -> verifyAddressed(from, message.addressed)
            else -> ErrorDtoOuterClass.ErrorDto.newBuilder()
                .setMessage("Invalid message signature")
                .build()
        }

    private fun verifyAnonymous(message: Message.SignedAnonymousMessage): ErrorDtoOuterClass.ErrorDto? =
        if (!validSignature(message)) {
            if (messageIsError(message.message)) {
                null
            } else {
                ErrorDtoOuterClass.ErrorDto.newBuilder()
                    .setMessage("Invalid message signature")
                    .build()
            }
        } else {
            null
        }

    private fun verifyAddressed(
        from: String,
        message: Message.SignedAddressedMessage
    ): ErrorDtoOuterClass.ErrorDto? =
        if (!validSignature(from, message)) {
            if (messageIsError(message.message)) {
                null
            } else {
                ErrorDtoOuterClass.ErrorDto.newBuilder()
                    .setMessage("Invalid message signature")
                    .build()
            }
        } else {
            null
        }

    private fun messageIsError(message: Any) =
        message.`is`(ErrorDtoOuterClass.ErrorDto::class.java)

    private fun validSignature(from: String, message: Message.SignedAddressedMessage) =
        web3Service.verifySignature(
            encoder.encodeToString(message.message.toByteArray()),
            message.signature,
            from
        )

    private fun validSignature(message: Message.SignedAnonymousMessage) =
        web3Service.verifySignature(
            encoder.encodeToString(message.message.toByteArray()),
            message.signature,
            message.address
        )
}
