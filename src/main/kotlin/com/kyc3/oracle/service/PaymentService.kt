package com.kyc3.oracle.service

import com.kyc3.oracle.payment.PaymentOuterClass
import com.kyc3.oracle.service.contract.CashierContractService
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val cashierContractService: CashierContractService
) {

    fun handleUserPayment(
        address: String,
        payment: PaymentOuterClass.Payment
    ) =
        cashierContractService.payment(address, payment)
}
