package com.kyc3.oracle.model

data class DecodedNftSettings(
    val provider: String,
    val price: Long,
    val type: Long,
    val expiration: Long,
)
