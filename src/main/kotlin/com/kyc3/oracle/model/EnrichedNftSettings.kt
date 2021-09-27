package com.kyc3.oracle.model

data class EnrichedNftSettings(
    val id: Long,
    val type: Int,
    val perpetuity: Boolean,
    val price: Int,
    val expiration: Long,
    val attestationProvider: String,
    val attestationEngine: String,
    val attestationProviderSignedMessage: String,
    val attestationEngineSignedMessage: String,
    val status: Boolean
)
