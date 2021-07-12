package com.kyc3.oracle.repository

import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.model.EnrichedNftSettings
import com.kyc3.oracle.types.Tables
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
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
        Tables.NFT_SETTINGS.PERPETUITY,
        Tables.NFT_SETTINGS.PRICE,
        Tables.NFT_SETTINGS.EXPIRATION,
        Tables.NFT_SETTINGS.SIGNED_MESSAGE,
      )
      .values(
        nft.apId,
        nft.type,
        nft.perpetuity,
        nft.price,
        nft.expiration,
        nft.signedMessage
      )
      .execute()

  fun findByNftType(nftType: Int): NftSettingsRecord? =
    dsl.selectFrom(Tables.NFT_SETTINGS)
      .where(Tables.NFT_SETTINGS.TYPE.eq(nftType))
      .fetchOne()

  fun findAll(): List<EnrichedNftSettings> =
    dsl.select(
      Tables.ATTESTATION_PROVIDER.ADDRESS,
      Tables.NFT_SETTINGS.TYPE,
      Tables.NFT_SETTINGS.PERPETUITY,
      Tables.NFT_SETTINGS.EXPIRATION,
      Tables.NFT_SETTINGS.PRICE,
      Tables.NFT_SETTINGS.SIGNED_MESSAGE
    )
      .from(Tables.NFT_SETTINGS)
      .join(Tables.ATTESTATION_PROVIDER)
      .on(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
      .fetch {
        EnrichedNftSettings(
          id = it.get(Tables.NFT_SETTINGS.ID),
          apAddress = it.get(Tables.ATTESTATION_PROVIDER.ADDRESS),
          type = it.get(Tables.NFT_SETTINGS.TYPE),
          perpetuity = it.get(Tables.NFT_SETTINGS.PERPETUITY),
          expiration = it.get(Tables.NFT_SETTINGS.EXPIRATION).toInstant(ZoneOffset.UTC).epochSecond,
          price = it.get(Tables.NFT_SETTINGS.PRICE),
          signedMessage = it.get(Tables.NFT_SETTINGS.SIGNED_MESSAGE),
          status = it.get(Tables.NFT_SETTINGS.STATUS),
        )
      }

  fun updateStatusById(dto: AttestationProviderOuterClass.ChangeNftSettingsStatusRequest): Int =
    dsl.update(Tables.NFT_SETTINGS)
      .set(Tables.NFT_SETTINGS.STATUS, dto.activate)
      .from(Tables.ATTESTATION_PROVIDER)
      .where(Tables.NFT_SETTINGS.ID.eq(dto.nftId))
      .and(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
      .and(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(dto.apAddress))
      .execute()
}