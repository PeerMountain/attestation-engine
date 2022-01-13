package com.kyc3.oracle.api.flow

import com.kyc3.Exchange
import com.kyc3.oracle.api.APIResponseService
import com.kyc3.oracle.model.UserKeys
import com.kyc3.oracle.service.ExchangeKeysHolder
import com.kyc3.oracle.service.UserKeysService
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExchangeMessageFlow(
    private val apiResponse: APIResponseService,
    private val userKeysService: UserKeysService,
    private val exchangeKeysHolder: ExchangeKeysHolder
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun exchange(from: EntityBareJid, chat: Chat, exchange: Exchange.ExchangeKeysRequest) {
        log.info("process='ExchangeMessageFlow' message='received keys' from='${from.localpart}'")
        userKeysService.store(
            from.asEntityBareJidString(),
            UserKeys(
                username = exchange.username,
                publicEncryptionKey = exchange.publicEncryptionKey,
                address = exchange.address
            )
        )
        apiResponse.responseToClient(
            chat,
            exchangeKeysHolder.generateExchangeMessageResponse()
        )
    }
}
