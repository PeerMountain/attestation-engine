package com.kyc3.oracle.model

import java.math.BigInteger

data class MintRequest(
    val address: String,
    val nonce: BigInteger,
    val encodedNftSettings: String,
    val encodedNftSettingsSigned: String,
    val encodedAttestationData: String,
    val encodedAttestationDataSigned: String
)
