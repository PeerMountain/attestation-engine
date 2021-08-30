package com.kyc3.oracle.api

import com.kyc3.oracle.api.flow.IncomingMessageManager
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OracleAPIListener(
  private val chatManager: ChatManager,
  private val incomingMessageManager: IncomingMessageManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='OracleAPIListener.listenToOracle' from='${from.asUnescapedString()}' message='received an event'")
      incomingMessageManager.incomingMessage(from, chat, message.body)
    }
  }
}