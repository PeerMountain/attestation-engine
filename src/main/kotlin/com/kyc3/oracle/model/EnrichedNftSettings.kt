package com.kyc3.oracle.model

data class EnrichedNftSettings(
    val id: Long,
    val apAddress: String,
    val type: Int,
    val perpetuity: Boolean,
    val price: Int,
    val expiration: Long,
    val signedMessage: String,
    val status: Boolean
)