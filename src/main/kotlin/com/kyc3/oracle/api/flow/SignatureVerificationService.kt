package com.kyc3.oracle.api.flow

import com.google.protobuf.Any
import com.kyc3.ErrorDtoOuterClass
import com.kyc3.Message
import com.kyc3.oracle.service.Web3Service
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.concurrent.CompletableFuture

@Service
class SignatureVerificationService(
    private val web3Service: Web3Service,
    private val encoder: Base64.Encoder
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun verify(from: String, message: Message.SignedMessage): CompletableFuture<ErrorDtoOuterClass.ErrorDto?> =
        when (message.bodyCase) {
            Message.SignedMessage.BodyCase.ANONYMOUS -> verifyAnonymous(message.anonymous)
            Message.SignedMessage.BodyCase.ADDRESSED -> verifyAddressed(from, message.addressed)
            else -> CompletableFuture.completedFuture(
                ErrorDtoOuterClass.ErrorDto.newBuilder()
                    .setMessage("Invalid message signature")
                    .build()
            )
        }

    private fun verifyAnonymous(message: Message.SignedAnonymousMessage): CompletableFuture<ErrorDtoOuterClass.ErrorDto?> =
        if (!validSignature(message)) {
            if (messageIsError(message.message)) {
                CompletableFuture.completedFuture(null as ErrorDtoOuterClass.ErrorDto?)
            } else {
                CompletableFuture.completedFuture(
                    ErrorDtoOuterClass.ErrorDto.newBuilder()
                        .setMessage("Invalid message signature")
                        .build()
                )
            }
        } else {
            CompletableFuture.completedFuture(null as ErrorDtoOuterClass.ErrorDto?)
        }

    private fun verifyAddressed(
        from: String,
        message: Message.SignedAddressedMessage
    ): CompletableFuture<ErrorDtoOuterClass.ErrorDto?> =
        if (!validSignature(from, message)) {
            if (messageIsError(message.message)) {
                CompletableFuture.completedFuture(null as ErrorDtoOuterClass.ErrorDto?)
            } else {
                log.info("process='SignatureVerificationService' message='invalid signature from user' type=")
                CompletableFuture.completedFuture(
                    ErrorDtoOuterClass.ErrorDto.newBuilder()
                        .setMessage("Invalid message signature")
                        .build()
                )
            }
        } else {
            CompletableFuture.completedFuture(null as ErrorDtoOuterClass.ErrorDto?)
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
