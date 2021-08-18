package com.kyc3.oracle.api

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.kyc3.MessageOuterClass
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service
import java.util.*

@Service
class OracleAPIResponse(
  private val base64Encoder: Base64.Encoder
){

  fun responseToClient(chat: Chat, message: GeneratedMessageV3) =
    MessageOuterClass.Message.newBuilder()
      .setType("type")
      .setMessage(Any.pack(message))
      .build()
      .toByteArray()
      .let { base64Encoder.encodeToString(it) }
      .let { chat.send(it) }

}
