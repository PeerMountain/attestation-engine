package com.kyc3.oracle.repository

import com.kyc3.oracle.ap.ChangeNftStatus
import com.kyc3.oracle.ap.ListNft
import com.kyc3.oracle.model.EnrichedNftSettings
import com.kyc3.oracle.types.Tables
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.jooq.DSLContext
import org.jooq.Record10
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class NftSettingsRepository(
    private val dsl: DSLContext
) {

    fun createNft(nft: NftSettingsRecord): Int =
        dsl.insertInto(Tables.NFT_SETTINGS)
            .columns(
                Tables.NFT_SETTINGS.AP_ID,
                Tables.NFT_SETTINGS.TYPE,
                Tables.NFT_SETTINGS.PRICE,
                Tables.NFT_SETTINGS.EXPIRATION,
                Tables.NFT_SETTINGS.SIGNATURE,
                Tables.NFT_SETTINGS.NAME,
                Tables.NFT_SETTINGS.DESCRIPTION,
                Tables.NFT_SETTINGS.IMAGE_URL,
            )
            .values(
                nft.apId,
                nft.type,
                nft.price,
                nft.expiration,
                nft.signature,
                nft.name,
                nft.description,
                nft.imageUrl,
            )
            .execute()

    fun findByNftType(nftType: Int): NftSettingsRecord? =
        dsl.selectFrom(Tables.NFT_SETTINGS)
            .where(Tables.NFT_SETTINGS.TYPE.eq(nftType))
            .fetchOne()

    fun searchFor(keywords: String): List<EnrichedNftSettings> =
        selectFromNftJoinedAttestationProvider()
            .on(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
            .where("? % ANY(STRING_TO_ARRAY(nft_settings.name,' ') || STRING_TO_ARRAY(nft_settings.description,' '))", keywords)
            .fetch { enrichedNftSettings(it) }

    fun findAll(apAddress: String, request: ListNft.ListNftRequest): List<EnrichedNftSettings> =
        selectFromNftJoinedAttestationProvider()
            .on(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
            .let { condition ->
                apAddress
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        condition.where(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(it))
                    }
                    ?: condition
            }
            .fetch { enrichedNftSettings(it) }

    fun updateStatusById(dto: ChangeNftStatus.ChangeNftSettingsStatusRequest): Int =
        dsl.update(Tables.NFT_SETTINGS)
            .set(Tables.NFT_SETTINGS.STATUS, dto.activate)
            .from(Tables.ATTESTATION_PROVIDER)
            .where(Tables.NFT_SETTINGS.ID.eq(dto.nftId))
            .and(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
            .and(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(dto.apAddress))
            .execute()

    private fun selectFromNftJoinedAttestationProvider() =
        dsl.select(
            Tables.NFT_SETTINGS.ID,
            Tables.ATTESTATION_PROVIDER.ADDRESS,
            Tables.NFT_SETTINGS.TYPE,
            Tables.NFT_SETTINGS.EXPIRATION,
            Tables.NFT_SETTINGS.PRICE,
            Tables.NFT_SETTINGS.SIGNATURE,
            Tables.NFT_SETTINGS.STATUS,
            Tables.NFT_SETTINGS.NAME,
            Tables.NFT_SETTINGS.DESCRIPTION,
            Tables.NFT_SETTINGS.IMAGE_URL,
        )
            .from(Tables.NFT_SETTINGS)
            .join(Tables.ATTESTATION_PROVIDER)

    private fun enrichedNftSettings(it: Record10<Long, String, Int, LocalDateTime, Int, String, Boolean, String, String, String>) =
        EnrichedNftSettings(
            id = it.get(Tables.NFT_SETTINGS.ID),
            type = it.get(Tables.NFT_SETTINGS.TYPE),
            expiration = it.get(Tables.NFT_SETTINGS.EXPIRATION).toInstant(ZoneOffset.UTC).epochSecond,
            price = it.get(Tables.NFT_SETTINGS.PRICE),
            attestationProvider = it.get(Tables.ATTESTATION_PROVIDER.ADDRESS),
            signature = it.get(Tables.NFT_SETTINGS.SIGNATURE),
            status = it.get(Tables.NFT_SETTINGS.STATUS),
            name = it.get(Tables.NFT_SETTINGS.NAME),
            description = it.get(Tables.NFT_SETTINGS.DESCRIPTION),
            imageUrl = it.get(Tables.NFT_SETTINGS.IMAGE_URL),
        )
}
