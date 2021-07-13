package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.AttestationProviderOuterClass
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.service.AttestationProviderService
import com.kyc3.oracle.user.ApList
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class AttestationProviderListListener(
  private val attestationProviderService: AttestationProviderService
) : OracleListener<ApList.AttestationProviderListRequest, ApList.AttestationProviderListResponse> {
  override fun type(): Class<ApList.AttestationProviderListRequest> =
    ApList.AttestationProviderListRequest::class.java

  override fun accept(event: Any, chat: Chat): ApList.AttestationProviderListResponse =
    attestationProviderService.findConfirmedProviders()
      .map {
        AttestationProviderOuterClass.AttestationProvider.newBuilder()
          .setName(it.name)
          .setAddress(it.address)
          .build()
      }
      .let {
        ApList.AttestationProviderListResponse.newBuilder()
          .addAllProviders(it)
          .build()
      }
}