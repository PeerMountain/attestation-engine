package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttestationProviderListener(
    private val attestationProviderService: AttestationProviderService
) : OracleListener<AttestationProviderOuterClass.AttestationProviderRegister> {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun type(): Class<AttestationProviderOuterClass.AttestationProviderRegister> =
      AttestationProviderOuterClass.AttestationProviderRegister::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type())
        .also {
          log.info("process='AttestationProviderListener' message='received message' event='${it}'")
          attestationProviderService.create(
              name = it.provider.name,
              address = it.provider.address,
              transaction = it.transaction
          )
        }
  }
}