package com.kyc3.oracle.api.router

import com.kyc3.ErrorDtoOuterClass
import com.kyc3.Message
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ErrorDtoListener : OracleListener<ErrorDtoOuterClass.ErrorDto, ErrorDtoOuterClass.ErrorDto> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<ErrorDtoOuterClass.ErrorDto> =
        ErrorDtoOuterClass.ErrorDto::class.java

    override fun accept(event: Message.SignedMessage, chat: Chat): ErrorDtoOuterClass.ErrorDto? {
        log.warn("Received an event with invalid signature")
        return null
    }
}
