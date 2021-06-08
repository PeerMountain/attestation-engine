package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ListNftSettingsListener(
    private val nftSettingsService: NftSettingsService,
    private val oracleAPIResponse: OracleAPIResponse,
) : OracleListener<AttestationProviderOuterClass.ListNftRequest> {
  override fun type(): Class<AttestationProviderOuterClass.ListNftRequest> =
      AttestationProviderOuterClass.ListNftRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    oracleAPIResponse.responseToClient(chat, nftSettingsService.getAllNft(event.unpack(type())))
  }
}