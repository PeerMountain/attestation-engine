package com.kyc3.oracle.model

data class AttestationDataDto(
  val providerAddress: String,
  val data: String,
  val hashKeyArray: String,
  val tokenUri: String,
  val hashedData: String
)