package com.kyc3.oracle.api.router

import com.kyc3.Message
import com.kyc3.oracle.ap.Register
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttestationProviderRegisterListener(
    private val attestationProviderService: AttestationProviderService
) : OracleListener<Register.RegisterAttestationProviderRequest, Register.RegisterAttestationProviderResponse> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<Register.RegisterAttestationProviderRequest> =
        Register.RegisterAttestationProviderRequest::class.java

    override fun accept(event: Message.SignedMessage, chat: Chat): Register.RegisterAttestationProviderResponse =
        event.message.unpack(type())
            .also {
                log.info("process='AttestationProviderListener' message='received message' event='$it'")
                attestationProviderService.create(
                    name = it.provider.name,
                    address = it.provider.address,
                    transaction = it.provider.initialTransaction
                )
            }
            .let { Register.RegisterAttestationProviderResponse.getDefaultInstance() }
}
