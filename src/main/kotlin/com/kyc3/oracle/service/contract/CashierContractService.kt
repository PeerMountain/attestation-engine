package com.kyc3.oracle.service.contract

import com.kyc3.CashierContractV2
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
            nonceService.nextNonce(),
            "0x9446cc89b7d8a2a53915f479ed367d7f8bfb9b5f",
            Numeric.hexStringToByteArray("0x000000000000000000000000000000000000000000000000000000000000007b00000000000000000000000000000000000000000000000000000000000001c8000000000000000000000000d1a01ccf70cd5be98f3dfeb36e8d943260f161db"),
            Numeric.hexStringToByteArray("0x1f7020cbe198b831b6250f0f357c40c066ec937afbc44706dedffac09163e809127f2c643cfd26dba1b0e57104ce052500d9c9f2fc6eacc5229619a44d94c1ab1b")
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
