package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.NftService
import com.kyc3.oracle.user.NftTransfer
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class NftTransferRequestListener(
    private val nftService: NftService
) : OracleAddressedListener<NftTransfer.NftTransferRequest, NftTransfer.NftTransferResponse> {
    override fun type(): Class<NftTransfer.NftTransferRequest> =
        NftTransfer.NftTransferRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): CompletableFuture<NftTransfer.NftTransferResponse> =
        nftService.nftTransfer(
            chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
            event.message.unpack(type())
        )
}
