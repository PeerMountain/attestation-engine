package com.kyc3.oracle.service

import com.kyc3.oracle.model.AttestationDataDto
import com.kyc3.oracle.model.EnrichedAttestationData
import com.kyc3.oracle.repository.AttestationDataRepository
import com.kyc3.oracle.repository.NftSettingsRepository
import com.kyc3.oracle.types.tables.records.AttestationDataRecord
import org.springframework.stereotype.Service

@Service
class AttestationDataService(
    private val attestationDataRepository: AttestationDataRepository,
    private val nftSettingsRepository: NftSettingsRepository
) {

    fun submitAttestationData(attestationData: AttestationDataDto) {
        nftSettingsRepository.findByNftType(attestationData.nftType)
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

    fun findDataForAttestation(apAddress: String): List<EnrichedAttestationData> =
        attestationDataRepository.findAllForProvider(apAddress)

    fun signAttestationData(id: Long, signedMessage: String) =
        attestationDataRepository.updateSignedMessage(id, signedMessage)

    fun findUserAttestations(customerAddress: String): List<EnrichedAttestationData> =
        attestationDataRepository.findByCustomerAddress(customerAddress)
}
