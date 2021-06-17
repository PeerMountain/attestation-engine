package com.kyc3.oracle.service

import com.kyc3.oracle.model.AttestationDataDto
import com.kyc3.oracle.repository.AttestationDataRepository
import com.kyc3.oracle.repository.AttestationProviderRepository
import com.kyc3.oracle.types.tables.records.AttestationDataRecord
import org.jooq.Result
import org.springframework.stereotype.Service

@Service
class AttestationDataService(
  private val attestationDataRepository: AttestationDataRepository,
  private val attestationProviderRepository: AttestationProviderRepository
) {

  fun submitAttestationData(attestationData: AttestationDataDto) {
    attestationProviderRepository.findByAddress(attestationData.providerAddress)
      ?.let {
        attestationDataRepository.create(
          AttestationDataRecord(
            null,
            it.id,
            attestationData.customerAddress,
            attestationData.data,
            attestationData.hashKeyArray,
            attestationData.tokenUri,
            attestationData.hashedData,
            null
          )
        )
      }
  }

  fun findDataForAttestation(apAddress: String) =
    attestationProviderRepository.findByAddress(apAddress)
      ?.let { attestationDataRepository.findByProviderId(it.id) }
      ?: emptyList()
}