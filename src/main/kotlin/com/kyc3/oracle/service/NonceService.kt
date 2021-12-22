package com.kyc3.oracle.service

import com.kyc3.CashierContractV2
import org.springframework.stereotype.Service
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

@Service
class NonceService(
    private val cashierContractV2: CashierContractV2,
    private val encoder: AbiEncoder,
    private val credentials: Credentials,
) {

    private lateinit var pow: BigInteger
    private val nonce = AtomicLong()

    fun proofOfWork() = pow

    fun nextNonce(): BigInteger =
        BigInteger.valueOf(nonce.incrementAndGet())

    @PostConstruct
    fun initializeNonce() {
        pow = BigInteger.valueOf(generateNonce())
    }

    fun generateNonce(): Long =
        with(cashierContractV2.proofOfWork.send().toLong()) {
            AtomicLong()
                .let { aeNonce ->
                    generateSequence { aeNonce.incrementAndGet() }
                        .find { possibleNonce ->
                            encoder.encodeProofOfWork(credentials.address, possibleNonce)
                                .let { Hash.sha3(it) }
                                .let { Numeric.hexStringToByteArray(it) }
                                .takeWhile { it == 0.toByte() }.count() >= this
                        }
                        ?: throw RuntimeException("AE Nonce can't be generated")
                }
        }
}
