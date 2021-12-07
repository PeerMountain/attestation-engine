package com.kyc3.oracle.service

import com.kyc3.oracle.ap.ChangeNftStatus
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.nft.Nft
import com.kyc3.oracle.nft.SignedNft
import com.kyc3.oracle.repository.AttestationProviderRepository
import com.kyc3.oracle.repository.NftSettingsRepository
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import com.kyc3.oracle.user.SearchNft
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class NftSettingsService(
    private val attestationProviderRepository: AttestationProviderRepository,
    private val nftRepository: NftSettingsRepository,
) {

    fun createNft(request: CreateNft.CreateNftRequest): Int? =
        attestationProviderRepository.findByAddress(request.nftSettings.attestationProvider)
            ?.let {
                nftRepository.createNft(
                    NftSettingsRecord(
                        null,
                        it.id,
                        request.nftSettings.type,
                        request.nftSettings.price,
                        LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(request.nftSettings.expiration),
                            ZoneId.of("UTC")
                        ),
                        request.nftSettings.signedMessage,
                        true,
                        request.nftSettings.name,
                        request.nftSettings.description,
                        request.nftSettings.imageUrl,
                    )
                )
            }

    fun getAllNft(): SearchNft.SearchNftResponse =
        nftRepository.findAll()
            .map {
                SignedNft.SignedNftSettings.newBuilder()
                    .setId(it.id)
                    .setNft(
                        Nft.NftSettings.newBuilder()
                            .setPrice(it.price)
                            .setSignedMessage(it.signature)
                            .setType(it.type)
                            .setExpiration(it.expiration)
                            .setAttestationProvider(it.attestationProvider)
                            .build()
                    )
                    .setStatus(it.status)
                    .build()
            }
            .let {
                SearchNft.SearchNftResponse.newBuilder()
                    .addAllNftSettingsList(it)
                    .build()
            }

    fun getAllNft(apAddress: String): ListNft.ListNftResponse =
        nftRepository.findAll(apAddress)
            .map {
                SignedNft.SignedNftSettings.newBuilder()
                    .setId(it.id)
                    .setNft(
                        Nft.NftSettings.newBuilder()
                            .setPrice(it.price)
                            .setSignedMessage(it.signature)
                            .setType(it.type)
                            .setExpiration(it.expiration)
                            .setAttestationProvider(it.attestationProvider)
                            .setName(it.name)
                            .setDescription(it.description)
                            .setImageUrl(it.imageUrl)
                            .build()
                    )
                    .setStatus(it.status)
                    .build()
            }
            .let {
                ListNft.ListNftResponse.newBuilder()
                    .addAllNftSettingsList(it)
                    .build()
            }

    fun searchNft(request: SearchNft.SearchNftRequest): SearchNft.SearchNftResponse =
        nftRepository.searchFor(request.keywords)
            .map {
                SignedNft.SignedNftSettings.newBuilder()
                    .setId(it.id)
                    .setNft(
                        Nft.NftSettings.newBuilder()
                            .setPrice(it.price)
                            .setSignedMessage(it.signature)
                            .setType(it.type)
                            .setExpiration(it.expiration)
                            .setAttestationProvider(it.attestationProvider)
                            .setName(it.name)
                            .setDescription(it.description)
                            .setImageUrl(it.imageUrl)
                            .build()
                    )
                    .setStatus(it.status)
                    .build()
            }
            .let {
                SearchNft.SearchNftResponse.newBuilder()
                    .addAllNftSettingsList(it)
                    .build()
            }

    fun changeNftStatus(dto: ChangeNftStatus.ChangeNftSettingsStatusRequest): Boolean =
        nftRepository.updateStatusById(dto) == 1
}
