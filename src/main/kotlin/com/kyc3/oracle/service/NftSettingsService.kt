package com.kyc3.oracle.service

import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.repository.AttestationProviderRepository
import com.kyc3.oracle.repository.NftRepository
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class NftSettingsService(
    private val attestationProviderRepository: AttestationProviderRepository,
    private val nftRepository: NftRepository
) {

  fun createNft(request: AttestationProviderOuterClass.CreateNftRequest): Int? =
      attestationProviderRepository.findByAddress(request.address)
          ?.let {
            nftRepository.createNft(NftSettingsRecord(
                null,
                it.id,
                request.type,
                request.perpetuity,
                request.price,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(request.expiration), ZoneId.systemDefault()),
                request.signedMessage
            ))
          }
}