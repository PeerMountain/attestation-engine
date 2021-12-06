package com.kyc3.oracle.service.contract

import com.kyc3.CashierContractV2
import com.kyc3.oracle.service.NonceService
import com.kyc3.oracle.user.Deposit
import com.kyc3.oracle.user.NftMint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Numeric
import java.math.BigInteger

@Service
class CashierContractService(
    private val cashierContractV2: CashierContractV2,
    private val nonceService: NonceService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun deposit(address: String, request: Deposit.DepositRequest): TransactionReceipt =
        cashierContractV2.deposit(
            nonceService.nextNonce(),
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
            nonceService.nextNonce(),
            address,
            Numeric.hexStringToByteArray(request.message),
            Numeric.hexStringToByteArray(request.signature)
        )
            .send()
            .also {
                log.info("process=CashierContractV2:nftMint account=$address receipt=$it")
            }
}
