package com.kyc3.oracle.api.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OracleRouter(
  private val listeners: List<OracleListener<*, *>>,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun route(message: Message.SignedMessage, chat: Chat): GeneratedMessageV3? =
    try {
      listeners.find { listener -> message.message.`is`(listener.type()) }
        .also {
          if (it == null) {
            log.warn("process='OracleRouter.route' message='Type can't be processed'")
          }
        }
        ?.accept(message, chat)
    } catch (ex: RuntimeException) {
      log.warn("Exception during message processing", ex)
      null
    }
}