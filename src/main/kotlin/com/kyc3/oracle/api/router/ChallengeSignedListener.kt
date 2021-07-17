package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.ap.challenge.VerifyChallenge
import com.kyc3.oracle.service.TimestampAPService
import com.kyc3.oracle.user.ChallengeSigned
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class ChallengeSignedListener(
  private val timestampAPService: TimestampAPService
) :
  OracleListener<ChallengeSigned.ChallengeSignedRequest, ChallengeSigned.ChallengeSignedResponse> {
  override fun type(): Class<ChallengeSigned.ChallengeSignedRequest> =
    ChallengeSigned.ChallengeSignedRequest::class.java

  override fun accept(event: Any, chat: Chat): ChallengeSigned.ChallengeSignedResponse? {
    event.unpack(type())
      .let {
        VerifyChallenge.VerifyChallengeRequest.newBuilder()
          .setChallenge(it.challenge)
          .setSignedChallenge(it.signedChallenge)
          .setUserAddress(it.userAddress)
          .build()
      }
      .let { timestampAPService.sendToProvider(it) }

    return null
  }
}