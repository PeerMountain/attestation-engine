package com.kyc3.oracle.api

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleRouter
import org.jivesoftware.smack.chat2.ChatManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class OracleAPIListener(
  private val chatManager: ChatManager,
  private val oracleRouter: OracleRouter,
  private val messageParser: MessageParser,
  private val oracleAPIResponse: OracleAPIResponse,
  private val signatureVerificationService: SignatureVerificationService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct
  fun listenToOracle() {
    chatManager.addIncomingListener { from, message, chat ->
      log.info("process='OracleAPIListener.listenToOracle' from='${from.asUnescapedString()}' message='received an event'")
      doIfValid(from.asEntityBareJidString(), messageParser.parseMessage(message)) { signed ->
        oracleRouter.route(signed, chat)
      }
        ?.let { oracleAPIResponse.responseToClient(chat, it) }
    }
  }

  fun doIfValid(
    from: String,
    message: Message.SignedMessage,
    consumer: (Message.SignedMessage) -> GeneratedMessageV3?
  ) =
    signatureVerificationService.verify(from, message)
      ?: consumer(message)
}