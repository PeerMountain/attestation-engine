package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftMintService
import com.kyc3.oracle.user.NftMint
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class NftMintRequestListener(
    private val nftMintService: NftMintService
) : OracleAddressedListener<NftMint.NftMintRequest, NftMint.NftMintResponse> {
    override fun type(): Class<NftMint.NftMintRequest> =
        NftMint.NftMintRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): NftMint.NftMintResponse? =
        nftMintService.nftMint(
            chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
            event.message.unpack(type())
        )
}
