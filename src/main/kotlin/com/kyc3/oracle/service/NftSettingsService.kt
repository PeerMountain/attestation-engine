package com.kyc3.oracle.service

import com.kyc3.oracle.ap.ChangeNftStatus
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.nft.Nft
import com.kyc3.oracle.repository.AttestationProviderRepository
import com.kyc3.oracle.repository.NftSettingsRepository
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class NftSettingsService(
    private val attestationProviderRepository: AttestationProviderRepository,
    private val nftRepository: NftSettingsRepository
) {

    fun createNft(request: CreateNft.CreateNftRequest): Int? =
        attestationProviderRepository.findByAddress(request.nftSettings.address)
            ?.let {
                nftRepository.createNft(
                    NftSettingsRecord(
                        null,
                        it.id,
                        request.nftSettings.type,
                        request.nftSettings.perpetuity,
                        request.nftSettings.price,
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(request.nftSettings.expiration), ZoneId.of("UTC")),
                        request.nftSettings.signedMessage,
                        true
                    )
                )
            }

    fun getAllNft(request: ListNft.ListNftRequest): ListNft.ListNftResponse =
        nftRepository.findAll(request)
            .map {
                Nft.NftSettings.newBuilder()
                    .setId(it.id)
                    .setAddress(it.apAddress)
                    .setPerpetuity(it.perpetuity)
                    .setPrice(it.price)
                    .setSignedMessage(it.signedMessage)
                    .setType(it.type)
                    .setExpiration(it.expiration)
                    .setStatus(it.status)
                    .build()
            }
            .let {
                ListNft.ListNftResponse.newBuilder()
                    .addAllNftSettingsList(it)
                    .build()
            }

    fun changeNftStatus(dto: ChangeNftStatus.ChangeNftSettingsStatusRequest): Boolean =
        nftRepository.updateStatusById(dto) == 1
}
