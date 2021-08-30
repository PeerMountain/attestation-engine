package com.kyc3.oracle.api.flow

import com.kyc3.Message
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageParser(
  private val base64Decoder: Base64.Decoder
) {

  fun parseSignedMessage(messagePayload: String): Message.SignedMessage =
    Message.SignedMessage.parseFrom(base64Decoder.decode(messagePayload))

  fun parseGeneralMessage(messagePayload: String): Message.GeneralMessage =
    Message.GeneralMessage.parseFrom(base64Decoder.decode(messagePayload))

}