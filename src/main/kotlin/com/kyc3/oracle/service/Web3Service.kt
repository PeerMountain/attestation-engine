package com.kyc3.oracle.service

import com.kyc3.oracle.repository.Web3Repository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.utils.Numeric
import java.util.Optional

@Service
class Web3Service(
    private val web3Repository: Web3Repository,
    private val ecKeyPair: ECKeyPair
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun isTransactionValid(transactionHash: String, providerAddress: String): Optional<Boolean> =
        web3Repository.getTransactionReceipt(transactionHash)
            .transactionReceipt
            .map {
                it.from == providerAddress &&
                    it.blockNumber != null &&
                    it.isStatusOK
            }

    fun verifySignature(body: String, signature: String, address: String): Boolean =
        SignatureHelper.fromString(signature)
            .let { signatureData ->
                Numeric.hexStringToByteArray(Hash.sha3String(body))
                    .let { data ->
                        Sign.signedPrefixedMessageToKey(data, signatureData)
                    }
            }
            .let { Keys.getAddress(it) }
            .let { address.contains(it, true) }

    fun sign(body: String): Sign.SignatureData =
        Sign.signPrefixedMessage(Numeric.hexStringToByteArray(Hash.sha3String(body)), ecKeyPair)

    fun signHex(body: String): Sign.SignatureData =
        Sign.signPrefixedMessage(Numeric.hexStringToByteArray(Hash.sha3(body)), ecKeyPair)

    fun sendRawTransaction(transaction: String): EthSendTransaction =
        web3Repository.submitTransaction(transaction)
            .also {
                log.info("process=Web3Service:sendRawTransaction transactionHash=${it.transactionHash}")
            }
}
