package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class AttestationProviderListListener(
  private val oracleAPIResponse: OracleAPIResponse,
  private val attestationProviderService: AttestationProviderService
) : OracleListener<AttestationProviderOuterClass.AttestationProviderListRequest> {
  override fun type(): Class<AttestationProviderOuterClass.AttestationProviderListRequest> =
    AttestationProviderOuterClass.AttestationProviderListRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    attestationProviderService.findConfirmedProviders()
      .map {
        AttestationProviderOuterClass.AttestationProvider.newBuilder()
          .setName(it.name)
          .setAddress(it.address)
          .build()
      }
      .let {
        AttestationProviderOuterClass.AttestationProviderListResponse.newBuilder()
          .addAllProviders(it)
          .build()
      }
      .let { oracleAPIResponse.responseToClient(chat, it) }
  }
}