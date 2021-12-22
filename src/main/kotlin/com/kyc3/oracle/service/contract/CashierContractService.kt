package com.kyc3.oracle.service.contract

import com.kyc3.CashierContractV2
import com.kyc3.oracle.payment.PaymentOuterClass
import com.kyc3.oracle.service.NonceService
import com.kyc3.oracle.user.Deposit
import com.kyc3.oracle.user.NftMint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Numeric

@Service
class CashierContractService(
    private val cashierContractV2: CashierContractV2,
    private val nonceService: NonceService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun deposit(address: String, request: Deposit.DepositRequest): TransactionReceipt =
        cashierContractV2.deposit(
            nonceService.proofOfWork(),
            address,
            Numeric.hexStringToByteArray(request.message),
            Numeric.hexStringToByteArray(request.signature)
        )
            .send()
            .also {
                log.info("process=CashierContractV2:deposit account=$address receipt=$it")
            }

    fun nftMint(address: String, request: NftMint.NftMintRequest): TransactionReceipt =
        cashierContractV2.nftMint(
            nonceService.proofOfWork(),
            address,
            Numeric.hexStringToByteArray(request.message),
            Numeric.hexStringToByteArray(request.signature)
        )
            .send()
            .also {
                log.info("process=CashierContractV2:nftMint apAddress=$address receipt=$it")
            }

    fun payment(address: String, payment: PaymentOuterClass.Payment): TransactionReceipt =
        cashierContractV2.payment(
            nonceService.proofOfWork(),
            address,
            Numeric.hexStringToByteArray(payment.message),
            Numeric.hexStringToByteArray(payment.signature)
        )
            .send()
            .also {
                log.info("process=CashierContractV2:payment customer=$address receipt=$it")
            }

    fun getTokenMintedEvent(transactionReceipt: TransactionReceipt): CashierContractV2.NewTokenMintedEventResponse? =
        cashierContractV2.getNewTokenMintedEvents(transactionReceipt).firstOrNull()

    fun leadingZeros(): Long =
        cashierContractV2.proofOfWork.send().toLong()
}
