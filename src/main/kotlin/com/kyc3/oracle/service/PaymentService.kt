package com.kyc3.oracle.service

import com.kyc3.oracle.payment.PaymentOuterClass
import com.kyc3.oracle.service.contract.CashierContractService
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.util.concurrent.CompletableFuture

@Service
class PaymentService(
    private val cashierContractService: CashierContractService
) {

    fun handleUserPayment(
        address: String,
        payment: PaymentOuterClass.Payment
    ): CompletableFuture<TransactionReceipt> =
        cashierContractService.payment(address, payment)
}
