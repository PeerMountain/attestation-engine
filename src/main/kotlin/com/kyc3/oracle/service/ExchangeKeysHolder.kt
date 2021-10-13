package com.kyc3.oracle.service

import com.kyc3.Exchange
import com.kyc3.oracle.config.properties.XmppProperties
import com.kyc3.oracle.model.LibsodiumPublicKey
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.utils.Numeric

@Component
class ExchangeKeysHolder(
    private val xmppProperties: XmppProperties,
    private val credentials: Credentials,
    private val ecKeyPair: ECKeyPair,
    private val libsodiumPublicKey: LibsodiumPublicKey,
) {

    fun generateExchangeMessageRequest(): Exchange.ExchangeKeysRequest =
        Exchange.ExchangeKeysRequest.newBuilder()
            .setUsername(xmppProperties.user.userName)
            .setAddress(credentials.address)
            .setPublicKey(Numeric.toHexStringNoPrefix(ecKeyPair.publicKey))
            .setPublicEncryptionKey(libsodiumPublicKey.publicKey)
            .build()

    fun generateExchangeMessageResponse(): Exchange.ExchangeKeysResponse =
        Exchange.ExchangeKeysResponse.newBuilder()
            .setUsername(xmppProperties.user.userName)
            .setAddress(credentials.address)
            .setPublicKey(Numeric.toHexStringNoPrefix(ecKeyPair.publicKey))
            .setPublicEncryptionKey(libsodiumPublicKey.publicKey)
            .build()
}
