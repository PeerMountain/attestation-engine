package com.kyc3.oracle.model

data class UserKeys(
    val username: String,
    val publicEncryptionKey: String,
    val address: String,
)
