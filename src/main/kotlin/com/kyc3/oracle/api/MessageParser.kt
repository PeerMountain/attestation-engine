package com.kyc3.oracle.api

import com.kyc3.Message
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageParser(
  private val base64Decoder: Base64.Decoder
) {

  fun parseMessage(message: SmackMessage): Message.SignedMessage =
    Message.SignedMessage.parseFrom(base64Decoder.decode(message.body))

}