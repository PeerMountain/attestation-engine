package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.Message
import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.oracle.service.OracleFrontService
import com.kyc3.oracle.user.ChallengeSigned
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class VerifyChallengeListener(
  private val oracleFrontService: OracleFrontService
) :
  OracleListener<VerifyChallenge.VerifyChallengeResponse, VerifyChallenge.VerifyChallengeResponse> {
  override fun type(): Class<VerifyChallenge.VerifyChallengeResponse> =
    VerifyChallenge.VerifyChallengeResponse::class.java

  override fun accept(event: Message.SignedMessage, chat: Chat): VerifyChallenge.VerifyChallengeResponse? {
    event.message.unpack(type())
      .let {
        oracleFrontService.sendToFrontend(
          it.userPublicKey,
          ChallengeSigned.ChallengeSignedResponse.newBuilder()
            .setRedirectUrl(it.redirectUrl)
            .build()
        )
      }
    return null
  }
}