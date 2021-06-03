package com.kyc3.oracle.service

import com.kyc3.oracle.repository.AttestationProviderRepository
import org.springframework.stereotype.Service

@Service
class AttestationProviderService(
    private val attestationProviderRepository: AttestationProviderRepository
) {

  fun create(
      name: String,
      address: String,
      transaction: String
  ) =
      attestationProviderRepository.create(name, address, transaction)
}