package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.service.OracleFrontService
import com.kyc3.oracle.user.InitiateNftPurchase
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class InitiateNFTPurchaseResponseListener(
  private val oracleFrontService: OracleFrontService
) :
  OracleListener<GenerateChallenge.GenerateChallengeResponse, InitiateNftPurchase.InitiateNFTPurchaseResponse> {
  override fun type(): Class<GenerateChallenge.GenerateChallengeResponse> =
    GenerateChallenge.GenerateChallengeResponse::class.java

  override fun accept(event: Any, chat: Chat): InitiateNftPurchase.InitiateNFTPurchaseResponse? {
    event.unpack(type())
      .let {
        oracleFrontService.sendToFrontend(
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