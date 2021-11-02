package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.OracleFrontService
import com.kyc3.oracle.user.InitiateNftPurchase
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class GenerateChallengeListener(
    private val oracleFrontService: OracleFrontService
) :
    OracleAddressedListener<GenerateChallenge.GenerateChallengeResponse, InitiateNftPurchase.InitiateNFTPurchaseResponse> {
    override fun type(): Class<GenerateChallenge.GenerateChallengeResponse> =
        GenerateChallenge.GenerateChallengeResponse::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): InitiateNftPurchase.InitiateNFTPurchaseResponse? {
        event.message.unpack(type())
            .let {
                oracleFrontService.sendToFrontend(
                    it.userAddress,
                    it.userPublicKey,
                    InitiateNftPurchase.InitiateNFTPurchaseResponse.newBuilder()
                        .setUserAddress(it.userAddress)
                        .setNftType(it.nftType)
                        .setChallenge(it.challenge.data)
                        .build()
                )
            }
        return null
    }
}
