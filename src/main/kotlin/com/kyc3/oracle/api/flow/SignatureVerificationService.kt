package com.kyc3.oracle.api.flow

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

    fun verify(
        from: String,
        message: Message.SignedMessage
    ): ErrorDtoOuterClass.ErrorDto? =
        if (!validSignature(message)) {
            if (messageIsError(message)) {
                null
            } else {
                ErrorDtoOuterClass.ErrorDto.newBuilder()
                    .setMessage("Invalid message signature")
                    .build()
            }
        } else {
            null
        }

    private fun messageIsError(message: Message.SignedMessage) =
        message.message.`is`(ErrorDtoOuterClass.ErrorDto::class.java)

    private fun validSignature(message: Message.SignedMessage) = web3Service.verifySignature(
        encoder.encodeToString(message.message.toByteArray()),
        message.signature,
        message.address
    )
}
