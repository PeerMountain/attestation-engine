package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ChangeNftSettingsStatusListener(
  private val apiResponse: OracleAPIResponse,
  private val nftSettingsService: NftSettingsService,
) :
  OracleListener<AttestationProviderOuterClass.ChangeNftSettingsStatusRequest> {
  override fun type(): Class<AttestationProviderOuterClass.ChangeNftSettingsStatusRequest> =
    AttestationProviderOuterClass.ChangeNftSettingsStatusRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    if (nftSettingsService.changeNftStatus(event.unpack(type()))) {
      apiResponse.responseToClient(
        chat,
        AttestationProviderOuterClass.ChangeNftSettingsStatusResponse.newBuilder()
          .build()
      )
    }
  }


}