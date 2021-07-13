package com.kyc3.oracle.api.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.oracle.OracleMessageOuterClass
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OracleRouter(
  private val listeners: List<OracleListener<*, *>>,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun route(byteArray: ByteArray, chat: Chat): GeneratedMessageV3? =
    OracleMessageOuterClass.OracleMessage.parseFrom(byteArray)
      .let { message ->
        try {
          listeners.find { listener -> message.message.`is`(listener.type()) }
            .also {
              if (it == null) {
                log.warn("process='OracleAPIListener.listenToOracle' message='Type can't be processed'")
              }
            }
            ?.accept(message.message, chat)
        } catch (ex: RuntimeException) {
          log.warn("Exception during message processing", ex)
          null
        }
      }
}