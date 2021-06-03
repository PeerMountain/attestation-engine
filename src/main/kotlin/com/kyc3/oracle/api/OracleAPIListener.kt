package com.kyc3.oracle.api

import com.kyc3.oracle.api.router.OracleRouter
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OracleAPIListener(
    private val chatManager: ChatManager,
    private val oracleRouter: OracleRouter,
    private val messageParser: MessageParser
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='OracleAPIListener' from='${from.asUnescapedString()}' message='received an event'")
      messageParser.parseMessage(message)
          .let { oracleRouter.route(it) }
    }
  }
}