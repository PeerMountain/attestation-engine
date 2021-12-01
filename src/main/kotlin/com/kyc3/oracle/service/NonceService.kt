package com.kyc3.oracle.service

import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicLong

@Service
class NonceService {

    private val nonce = AtomicLong(105501)

    fun nextNonce(): BigInteger =
        BigInteger.valueOf(nonce.incrementAndGet())
}
