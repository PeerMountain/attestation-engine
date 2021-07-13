package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import org.jivesoftware.smack.chat2.Chat

interface OracleListener<Request : GeneratedMessageV3, Response: GeneratedMessageV3> {
  fun type(): Class<Request>

  fun accept(event: Any, chat: Chat) : Response
}