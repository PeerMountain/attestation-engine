package com.kyc3.oracle.api

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.kyc3.oracle.OracleMessageOuterClass
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service
import java.util.*

@Service
class OracleAPIResponse {

  fun responseToClient(chat: Chat, message: GeneratedMessageV3) =
      OracleMessageOuterClass.OracleMessage.newBuilder()
          .setType("type")
          .setMessage(Any.pack(message))
          .build()
          .toByteArray()
          .let { Arrays.toString(it) }
          .let { chat.send(it) }

}
