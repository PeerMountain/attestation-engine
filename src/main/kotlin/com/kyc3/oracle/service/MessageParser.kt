package com.kyc3.oracle.service

import org.springframework.stereotype.Service
import org.jivesoftware.smack.packet.Message

@Service
class MessageParser {

  fun parseMessage(message: Message): ByteArray =
      message.body.split(",").map { it.toUByte().toByte() }.toByteArray()

}