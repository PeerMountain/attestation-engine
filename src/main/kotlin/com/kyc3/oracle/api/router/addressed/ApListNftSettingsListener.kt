package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ApListNftSettingsListener(
    private val nftSettingsService: NftSettingsService,
) : OracleAddressedListener<ListNft.ListNftRequest, ListNft.ListNftResponse> {
    override fun type(): Class<ListNft.ListNftRequest> =
        ListNft.ListNftRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): ListNft.ListNftResponse =
        nftSettingsService.getAllNft(
            chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
            event.message.unpack(type())
        )
}
