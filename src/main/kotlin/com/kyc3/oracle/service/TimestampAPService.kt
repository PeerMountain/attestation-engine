package com.kyc3.oracle.service

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.api.OracleAPIResponse
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.springframework.stereotype.Service

@Service
class TimestampAPService(
  private val oracleAPIResponse: OracleAPIResponse,
  private val chatManager: ChatManager
) {

  private val jid: EntityBareJid = JidCreate.entityBareFrom("timestamp-ap@jabber.hot-chilli.net")
  private val chat: Chat = chatManager.chatWith(jid)

  fun generateChallenge(userAddress: String, nftType: Int): Unit =
    oracleAPIResponse.responseToClient(
      chat, GenerateChallenge.GenerateChallengeRequest.newBuilder()
        .setUserAddress(userAddress)
        .setNftType(nftType)
        .build()
    )

  fun sendToProvider(message: GeneratedMessageV3): Unit =
    oracleAPIResponse.responseToClient(chat, message)
}