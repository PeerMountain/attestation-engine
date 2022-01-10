package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.InvoiceService
import com.kyc3.oracle.service.OracleFrontService
import com.kyc3.oracle.user.InitiateNftPurchase
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class GenerateChallengeListener(
    private val oracleFrontService: OracleFrontService,
    private val invoiceService: InvoiceService
) :
    OracleAddressedListener<GenerateChallenge.GenerateChallengeResponse, InitiateNftPurchase.InitiateNFTPurchaseResponse> {
    override fun type(): Class<GenerateChallenge.GenerateChallengeResponse> =
        GenerateChallenge.GenerateChallengeResponse::class.java

    override fun acceptSync(
        event: Message.SignedAddressedMessage,
        chat: Chat
    ): InitiateNftPurchase.InitiateNFTPurchaseResponse? =
        event.message.unpack(type())
            .let { apResponse ->
                invoiceService.generateInvoice(
                    chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
                    apResponse
                )
                    ?.also {
                        oracleFrontService.sendToFrontend(
                            apResponse.userAddress,
                            apResponse.userPublicKey,
                            it
                        )
                    }
            }
            .let { null }
}
