package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class SignedAttestationDataRequestListener(
  private val attestationDataService: AttestationDataService
) : OracleListener<AttestationProviderOuterClass.SignedAttestationDataRequest> {
  override fun type(): Class<AttestationProviderOuterClass.SignedAttestationDataRequest> =
    AttestationProviderOuterClass.SignedAttestationDataRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type())
      .let { attestationDataService.signAttestationData(it.attestationDataId, it.signedMessage) }
  }
}