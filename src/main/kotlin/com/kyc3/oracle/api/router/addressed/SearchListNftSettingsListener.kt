package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftSettingsService
import com.kyc3.oracle.user.SearchNft
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class SearchListNftSettingsListener(
    private val nftSettingsService: NftSettingsService,
) : OracleAddressedListener<SearchNft.SearchNftRequest, SearchNft.SearchNftResponse> {
    override fun type(): Class<SearchNft.SearchNftRequest> =
        SearchNft.SearchNftRequest::class.java

    override fun acceptSync(event: Message.SignedAddressedMessage, chat: Chat): SearchNft.SearchNftResponse =
        event.message.unpack(type())
            .let {
                if (it.keywords.isBlank()) {
                    nftSettingsService.getAllNft()
                } else {
                    nftSettingsService.searchNft(it)
                }
            }
}
