package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import org.jivesoftware.smack.chat2.Chat

interface OracleListener<T : GeneratedMessageV3> {
  fun type(): Class<T>

  fun accept(event: Any, chat: Chat)
}