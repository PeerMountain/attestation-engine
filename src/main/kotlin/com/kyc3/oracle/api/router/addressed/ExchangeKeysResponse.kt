package com.kyc3.oracle.api.router.addressed

import com.kyc3.Exchange
import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.model.UserKeys
import com.kyc3.oracle.service.UserKeysService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExchangeKeysResponse(
    private val userKeysService: UserKeysService
) : OracleAddressedListener<Exchange.ExchangeKeysResponse, Exchange.ExchangeKeysResponse> {
    override fun type(): Class<Exchange.ExchangeKeysResponse> =
        Exchange.ExchangeKeysResponse::class.java

    private val log = LoggerFactory.getLogger(javaClass)

    override fun acceptSync(event: Message.SignedAddressedMessage, chat: Chat): Exchange.ExchangeKeysResponse? =
        event.message.unpack(type())
            .let {
                log.info("process='ExchangeMessageFlow' message='received keys' from='${chat.xmppAddressOfChatPartner.localpart}'")
                userKeysService.store(
                    chat.xmppAddressOfChatPartner.asEntityBareJidString(),
                    UserKeys(
                        address = it.address,
                        username = it.username,
                        publicEncryptionKey = it.publicEncryptionKey
                    )
                )
            }
            .let { null }
}
