package com.kyc3.oracle.repository

import com.kyc3.oracle.types.Tables
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class NftRepository(
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
}