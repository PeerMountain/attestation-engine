package com.kyc3.oracle.api.flow

import com.kyc3.Exchange
import com.kyc3.Message
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.config.properties.XmppProperties
import com.kyc3.oracle.model.LibsodiumPublicKey
import com.kyc3.oracle.model.UserKeys
import com.kyc3.oracle.service.ExchangeKeysHolder
import com.kyc3.oracle.service.UserKeysService
import org.jivesoftware.smack.chat2.Chat
import org.jxmpp.jid.EntityBareJid
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.utils.Numeric

@Service
class ExchangeMessageFlow(
  private val apiResponse: OracleAPIResponse,
  private val userKeysService: UserKeysService,
  private val exchangeKeysHolder: ExchangeKeysHolder
) {

  fun exchange(from: EntityBareJid, chat: Chat, exchange: Exchange.ExchangeKeysRequest) {
    userKeysService.store(
      from.asEntityBareJidString(),
      UserKeys(
        username = exchange.username,
        publicEncryptionKey = exchange.publicEncryptionKey,
        address = exchange.address
      )
    )
    apiResponse.responseToClient(
      exchange.publicEncryptionKey,
      chat,
      exchangeKeysHolder.generateExchangeMessageResponse()
    )
  }
}