package com.kyc3.oracle.repository

import org.springframework.stereotype.Repository
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt
import org.web3j.protocol.core.methods.response.EthSendTransaction


@Repository
class Web3Repository(
    private val web3j: Web3j
) {

    fun getTransactionReceipt(transactionHash: String): EthGetTransactionReceipt =
        web3j.ethGetTransactionReceipt(transactionHash)
            .send()

    fun submitTransaction(transaction: String): EthSendTransaction =
        web3j.ethSendRawTransaction(transaction).send()
}
