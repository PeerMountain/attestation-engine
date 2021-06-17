package com.kyc3.oracle.service

import com.kyc3.oracle.repository.AttestationProviderRepository
import com.kyc3.oracle.types.tables.records.AttestationProviderRecord
import org.springframework.stereotype.Service

@Service
class AttestationProviderService(
  private val attestationProviderRepository: AttestationProviderRepository,
  private val web3Service: Web3Service
) {

  fun create(
    name: String,
    address: String,
    transaction: String
  ) =
    attestationProviderRepository.create(name, address, transaction)

  fun getProviderByAddress(address: String): AttestationProviderRecord? =
    attestationProviderRepository.findByAddress(address)

  fun confirmRegistration(name: String, address: String): Boolean =
    attestationProviderRepository.changeStatus(name, address, "CONFIRMED")

  fun failRegistration(name: String, address: String): Boolean =
    attestationProviderRepository.changeStatus(name, address, "FAILED")


  fun findProvidersToProcess() =
    attestationProviderRepository.findFirstWithStatus(
      10,
      "IN_PROGRESS"
    )

  fun findConfirmedProviders() =
    attestationProviderRepository.findFirstWithStatus(
      10,
      "CONFIRMED"
    )

  fun validateProviderRegistration(provider: AttestationProviderRecord) {
    web3Service.isTransactionValid(provider.initialTransaction, provider.address)
      .ifPresent {
        if (it) {
          confirmRegistration(provider.name, provider.address)
        } else {
          failRegistration(provider.name, provider.address)
        }
      }
  }
}