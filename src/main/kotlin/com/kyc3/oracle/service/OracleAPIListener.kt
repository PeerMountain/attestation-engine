package com.kyc3.oracle.service

import org.jivesoftware.smack.chat2.ChatManager
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OracleAPIListener(
    val chatManager: ChatManager
) {

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      println(message.body)
    }
  }
}