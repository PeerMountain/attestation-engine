package com.kyc3.oracle.model

data class EthEncryptedData(
    val version: String,
    val nonce: String,
    val ephemPublicKey: String,
    val cipherText: String
)
