package com.kyc3.oracle.api.router

import com.google.protobuf.GeneratedMessageV3
import org.jivesoftware.smack.chat2.Chat

interface OracleListener<Message : GeneratedMessageV3, Request : GeneratedMessageV3, Response : GeneratedMessageV3> {

    fun type(): Class<Request>

    fun accept(event: Message, chat: Chat): Response?
}
