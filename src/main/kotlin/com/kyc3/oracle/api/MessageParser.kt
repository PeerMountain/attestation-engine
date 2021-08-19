package com.kyc3.oracle.api

import org.jivesoftware.smack.packet.Message
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageParser(
  private val base64Decoder: Base64.Decoder
) {

  fun parseMessage(message: Message): ByteArray =
    base64Decoder.decode(message.body)

}