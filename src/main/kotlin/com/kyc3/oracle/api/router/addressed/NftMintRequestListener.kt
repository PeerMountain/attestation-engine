package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftService
import com.kyc3.oracle.user.NftMint
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class NftMintRequestListener(
    private val nftService: NftService
) : OracleAddressedListener<NftMint.NftMintRequest, NftMint.NftMintResponse> {
    override fun type(): Class<NftMint.NftMintRequest> =
        NftMint.NftMintRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): CompletableFuture<NftMint.NftMintResponse> =
        nftService.nftMint(
            chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
            event.message.unpack(type())
        )
}
