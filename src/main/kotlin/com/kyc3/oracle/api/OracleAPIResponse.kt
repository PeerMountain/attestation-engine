package com.kyc3.oracle.api

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.oracle.service.SignatureHelper
import com.kyc3.oracle.service.Web3Service
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import java.util.*

@Service
class OracleAPIResponse(
  private val base64Encoder: Base64.Encoder,
  private val ecKeyPair: ECKeyPair,
  private val web3Service: Web3Service
) {

  fun responseToClient(chat: Chat, message: GeneratedMessageV3) =
    Any.pack(message)
      .let {
        Message.SignedMessage.newBuilder()
          .setMessage(it)
          .setAddress(Keys.getAddress(ecKeyPair))
          .setSignature(SignatureHelper.toString(web3Service.sign(encodeMessage(it))))
          .build()
      }
      .toByteArray()
      .let { base64Encoder.encodeToString(it) }
      .let { chat.send(it) }

  fun encodeMessage(message: Any): String =
    base64Encoder.encodeToString(message.toByteArray())
}
