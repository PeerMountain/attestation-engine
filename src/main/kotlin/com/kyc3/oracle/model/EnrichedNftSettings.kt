package com.kyc3.oracle.model

data class EnrichedNftSettings(
    val id: Long,
    val type: Int,
    val price: Int,
    val expiration: Long,
    val attestationProvider: String,
    val attestationProviderSignedMessage: String,
    val status: Boolean
)
