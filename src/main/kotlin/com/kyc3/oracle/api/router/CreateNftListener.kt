package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class CreateNftListener(
    private val nftService: NftSettingsService
) : OracleListener<AttestationProviderOuterClass.CreateNftRequest> {
  override fun type(): Class<AttestationProviderOuterClass.CreateNftRequest> =
      AttestationProviderOuterClass.CreateNftRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    nftService.createNft(event.unpack(AttestationProviderOuterClass.CreateNftRequest::class.java))
  }
}