package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.contract.CashierContractService
import com.kyc3.oracle.user.Deposit
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class DepositRequestListener(
    private val cashierContractService: CashierContractService
) :
    OracleAddressedListener<Deposit.DepositRequest, Deposit.DepositResponse> {
    override fun type(): Class<Deposit.DepositRequest> =
        Deposit.DepositRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): Deposit.DepositResponse? =
        cashierContractService.deposit(
            chat.xmppAddressOfChatPartner.localpart.asUnescapedString(),
            event.message.unpack(type())
        )
            .let {
                Deposit.DepositResponse.newBuilder()
                    .setDepositTransactionHash(it.transactionHash)
                    .build()
            }

}