package com.kyc3.oracle.model

import java.math.BigInteger

data class TransferRequest(
    val address: String,
    val tokenId: Long,
    val nonce: BigInteger,
    val cashierAddress: String,
)
