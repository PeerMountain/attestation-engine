package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.SignAttestation
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class SignedAttestationDataRequestListener(
  private val attestationDataService: AttestationDataService
) :
  OracleListener<SignAttestation.SignAttestationDataRequest, SignAttestation.SignAttestationDataResponse> {
  override fun type(): Class<SignAttestation.SignAttestationDataRequest> =
    SignAttestation.SignAttestationDataRequest::class.java

  override fun accept(event: Any, chat: Chat): SignAttestation.SignAttestationDataResponse =
    event.unpack(type())
      .let { attestationDataService.signAttestationData(it.attestationDataId, it.signedMessage) }
      .let { SignAttestation.SignAttestationDataResponse.newBuilder().build() }
}