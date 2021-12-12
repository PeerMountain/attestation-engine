package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.PaymentService
import com.kyc3.oracle.service.TimestampAPService
import com.kyc3.oracle.user.ChallengeSigned
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ChallengeSignedListener(
    private val timestampAPService: TimestampAPService,
    private val paymentService: PaymentService
) :
    OracleAddressedListener<ChallengeSigned.ChallengeSignedRequest, ChallengeSigned.ChallengeSignedResponse> {
    override fun type(): Class<ChallengeSigned.ChallengeSignedRequest> =
        ChallengeSigned.ChallengeSignedRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): ChallengeSigned.ChallengeSignedResponse? {
        event.message.unpack(type())
            .also {
                paymentService.handleUserPayment(
                    chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
                    it.payment
                )
            }
            .let {
                VerifyChallenge.VerifyChallengeRequest.newBuilder()
                    .setChallenge(it.challenge)
                    .setSignedChallenge(it.signedChallenge)
                    .setUserAddress(it.userAddress)
                    .setUserPublicKey(event.publicKey)
                    .build()
            }
            .let { timestampAPService.sendToProvider(it) }

        return null
    }
}
