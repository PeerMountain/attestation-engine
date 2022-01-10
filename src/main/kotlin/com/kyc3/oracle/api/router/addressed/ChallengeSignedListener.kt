package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.PaymentService
import com.kyc3.oracle.service.TimestampAPService
import com.kyc3.oracle.user.ChallengeSigned
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ChallengeSignedListener(
    private val timestampAPService: TimestampAPService,
    private val paymentService: PaymentService
) :
    OracleAddressedListener<ChallengeSigned.ChallengeSignedRequest, ChallengeSigned.ChallengeSignedResponse> {
    override fun type(): Class<ChallengeSigned.ChallengeSignedRequest> =
        ChallengeSigned.ChallengeSignedRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): CompletableFuture<ChallengeSigned.ChallengeSignedResponse> =
        event.message.unpack(type())
            .let { challenge ->
                paymentService.handleUserPayment(
                    chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
                    challenge.payment
                )
                    .thenApply {
                        VerifyChallenge.VerifyChallengeRequest.newBuilder()
                            .setChallenge(challenge.challenge)
                            .setSignedChallenge(challenge.signedChallenge)
                            .setUserAddress(challenge.userAddress)
                            .setUserPublicKey(event.publicKey)
                            .build()
                    }
                    .thenAccept { timestampAPService.sendToProvider(it) }
                    .thenApply { null }
            }
}
