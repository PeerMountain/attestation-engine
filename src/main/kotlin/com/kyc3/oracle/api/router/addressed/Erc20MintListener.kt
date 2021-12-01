package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.Web3Service
import com.kyc3.oracle.user.Erc20
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class Erc20MintListener(
    private val web3Service: Web3Service
) : OracleAddressedListener<Erc20.Erc20MintRequest, Erc20.Erc20MintResponse> {
    override fun type(): Class<Erc20.Erc20MintRequest> =
        Erc20.Erc20MintRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): Erc20.Erc20MintResponse? =
        event.message.unpack(type())
            .let {
                web3Service.sendRawTransaction(it.signedTransaction)
            }
            .let {
                Erc20.Erc20MintResponse.newBuilder()
                    .setTransactionHash(it.transactionHash)
                    .build()
            }

}