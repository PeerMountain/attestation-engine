package com.kyc3.oracle.api.router

import com.kyc3.oracle.OracleMessageOuterClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OracleRouter(
    private val listeners: List<OracleListener<*>>
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun route(byteArray: ByteArray) =
      OracleMessageOuterClass.OracleMessage.parseFrom(byteArray)
          .let { message ->
            listeners.find { listener -> message.message.`is`(listener.type()) }
                ?.accept(message.message)
                ?: run {
                  log.warn("process='OracleRouter.route' message='Type can't be processed'")
                }
          }
}