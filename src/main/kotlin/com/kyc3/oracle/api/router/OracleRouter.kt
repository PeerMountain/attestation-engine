package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Internal
import com.kyc3.Message
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OracleRouter(
    private val addressedListeners: List<OracleAddressedListener<*, *>>,
    private val anonymousListeners: List<OracleAnonymousListener<*, *>>,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun route(message: Message.SignedMessage, chat: Chat): GeneratedMessageV3? =
        try {
            when (message.bodyCase) {
                Message.SignedMessage.BodyCase.ADDRESSED ->
                    route(addressedListeners, chat, { message.addressed.message }) {
                        message.addressed
                    }
                Message.SignedMessage.BodyCase.ANONYMOUS ->
                    route(anonymousListeners, chat, { message.anonymous.message }) {
                        message.anonymous
                    }
                else -> throw IllegalStateException()
            }
        } catch (ex: RuntimeException) {
            log.warn("Exception during message processing", ex)
            null
        }

    private fun <T : GeneratedMessageV3> route(
        listeners: List<OracleListener<T, *, *>>,
        chat: Chat,
        bodySupplier: () -> Any,
        signedMessageSupplier: () -> T
    ) = listeners.find { listener ->

        val res = bodySupplier()
        log.info("trying ${Internal.getDefaultInstance(listener.type()).descriptorForType.fullName} over ${res.typeUrl.let {
            val pos: Int = it.lastIndexOf('/')
            if (pos == -1) "" else it.substring(pos + 1)
        }}")
        res.`is`(listener.type())

    }
        .also {
            if (it == null) {
                log.warn("process='OracleRouter.route' message='Type can't be processed'")
                log.warn(bodySupplier().typeUrl)
                log.warn("body=${bodySupplier()}")
            }
        }
        ?.also { log.info("process='OracleRouter.route' type='${it.type()}' typeUrl=${bodySupplier().typeUrl}") }
        ?.accept(signedMessageSupplier(), chat)
}
