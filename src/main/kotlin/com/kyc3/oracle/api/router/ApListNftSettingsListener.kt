package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ApListNftSettingsListener(
  private val nftSettingsService: NftSettingsService,
) : OracleListener<ListNft.ListNftRequest, ListNft.ListNftResponse> {
  override fun type(): Class<ListNft.ListNftRequest> =
    ListNft.ListNftRequest::class.java

  override fun accept(event: Any, chat: Chat): ListNft.ListNftResponse =
    nftSettingsService.getAllNft(event.unpack(type()))
}