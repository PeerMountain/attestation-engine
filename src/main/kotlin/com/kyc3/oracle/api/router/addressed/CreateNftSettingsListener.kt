package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class CreateNftSettingsListener(
    private val nftService: NftSettingsService
) : OracleAddressedListener<CreateNft.CreateNftRequest, CreateNft.CreateNftResponse> {
    override fun type(): Class<CreateNft.CreateNftRequest> =
        CreateNft.CreateNftRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): CreateNft.CreateNftResponse =
        nftService.createNft(event.message.unpack(type()))
            .let { CreateNft.CreateNftResponse.getDefaultInstance() }
}
