package com.kyc3.oracle.model

data class AttestationEngineEncodeNftRequest(
    val address: String,
    val encodedSettings: String,
    val signedSettings: String,
)
