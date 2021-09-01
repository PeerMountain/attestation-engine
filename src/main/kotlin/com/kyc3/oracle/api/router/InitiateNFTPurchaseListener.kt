package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.Message
import com.kyc3.oracle.service.TimestampAPService
import com.kyc3.oracle.user.InitiateNftPurchase
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class InitiateNFTPurchaseListener(
    private val timestampAPService: TimestampAPService
) :
    OracleListener<InitiateNftPurchase.InitiateNFTPurchaseRequest, Any> {
    override fun type(): Class<InitiateNftPurchase.InitiateNFTPurchaseRequest> =
        InitiateNftPurchase.InitiateNFTPurchaseRequest::class.java

    override fun accept(event: Message.SignedMessage, chat: Chat): Any? {
        event.message.unpack(type()).let {
            timestampAPService.generateChallenge(it.userAddress, event.publicKey, it.nftType)
        }
        return null
    }
}