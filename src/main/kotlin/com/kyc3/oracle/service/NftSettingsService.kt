package com.kyc3.oracle.service

import com.kyc3.oracle.ap.ChangeNftStatus
import com.kyc3.oracle.ap.CreateNft
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.model.AttestationEngineEncodeNftRequest
import com.kyc3.oracle.nft.Nft
import com.kyc3.oracle.nft.SignedNft
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
    private val nftRepository: NftSettingsRepository,
    private val abiEncoder: AbiEncoder,
    private val web3Service: Web3Service
) {

    fun createNft(request: CreateNft.CreateNftRequest): Int? =
        attestationProviderRepository.findByAddress(request.nftSettings.attestationProvider)
            ?.let {
                nftRepository.createNft(
                    NftSettingsRecord(
                        null,
                        it.id,
                        request.nftSettings.type,
                        request.nftSettings.perpetuity,
                        request.nftSettings.price,
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(request.nftSettings.expiration), ZoneId.of("UTC")),
                        request.nftSettings.attestationEngine,
                        request.nftSettings.signedMessage,
                        signNftSettings(request),
                        true
                    )
                )
            }

    fun getAllNft(request: ListNft.ListNftRequest): ListNft.ListNftResponse =
        nftRepository.findAll(request)
            .map {
                SignedNft.SignedNftSettings.newBuilder()
                    .setId(it.id)
                    .setNft(
                        Nft.NftSettings.newBuilder()
                            .setPerpetuity(it.perpetuity)
                            .setPrice(it.price)
                            .setSignedMessage(it.attestationProviderSignedMessage)
                            .setType(it.type)
                            .setExpiration(it.expiration)
                            .setAttestationEngine(it.attestationEngine)
                            .setAttestationProvider(it.attestationProvider)
                            .build()
                    )
                    .setAttestationEngineSignature(it.attestationEngineSignedMessage)
                    .setStatus(it.status)
                    .build()
            }
            .let {
                ListNft.ListNftResponse.newBuilder()
                    .addAllNftSettingsList(it)
                    .build()
            }

    fun signNftSettings(request: CreateNft.CreateNftRequest): String =
        abiEncoder.encodeNftSettings(request.nftSettings)
            .let {
                abiEncoder.engineEncodeNftSettings(
                    AttestationEngineEncodeNftRequest(
                        address = request.nftSettings.attestationEngine,
                        encodedSettings = it,
                        signedSettings = request.nftSettings.signedMessage
                    )
                )
            }
            .let { web3Service.signHex("0x$it") }
            .let { SignatureHelper.toString(it) }

    fun changeNftStatus(dto: ChangeNftStatus.ChangeNftSettingsStatusRequest): Boolean =
        nftRepository.updateStatusById(dto) == 1
}
