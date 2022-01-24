package com.kyc3.oracle.repository

import com.kyc3.oracle.nft.TokenOuterClass
import com.kyc3.oracle.types.tables.TokenData
import com.kyc3.oracle.types.tables.records.TokenDataRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class TokenDataRepository(
    private val dsl: DSLContext
) {

    fun insertTokenData(tokenDataRecord: TokenDataRecord): Int =
        dsl.insertInto(TokenData.TOKEN_DATA)
            .columns(
                TokenData.TOKEN_DATA.HOLDER,
                TokenData.TOKEN_DATA.TOKEN_ID,
                TokenData.TOKEN_DATA.NFT_TYPE,
                TokenData.TOKEN_DATA.TOKEN_URI,
                TokenData.TOKEN_DATA.KEYS,
                TokenData.TOKEN_DATA.SETTINGS,
                TokenData.TOKEN_DATA.SETTINGS_HASH,
                TokenData.TOKEN_DATA.PROVIDER,
                TokenData.TOKEN_DATA.DATA
            )
            .values(
                tokenDataRecord.holder,
                tokenDataRecord.tokenId,
                tokenDataRecord.nftType,
                tokenDataRecord.tokenUri,
                tokenDataRecord.keys,
                tokenDataRecord.settings,
                tokenDataRecord.settingsHash,
                tokenDataRecord.provider,
                tokenDataRecord.data,
            )
            .execute()

    fun changeOwnership(tokenId: Long, oldHolder: String, newHolder: String): Int =
        dsl.update(TokenData.TOKEN_DATA)
            .set(TokenData.TOKEN_DATA.HOLDER, newHolder)
            .where(TokenData.TOKEN_DATA.HOLDER.eq(oldHolder))
            .and(TokenData.TOKEN_DATA.TOKEN_ID.eq(tokenId))
            .execute()

    fun findAllByHolder(userAddress: String): List<TokenDataRecord> =
        dsl.select(
            TokenData.TOKEN_DATA.HOLDER,
            TokenData.TOKEN_DATA.TOKEN_ID,
            TokenData.TOKEN_DATA.TOKEN_URI,
            TokenData.TOKEN_DATA.NFT_TYPE,
            TokenData.TOKEN_DATA.KEYS,
            TokenData.TOKEN_DATA.SETTINGS,
            TokenData.TOKEN_DATA.SETTINGS_HASH,
            TokenData.TOKEN_DATA.PROVIDER,
            TokenData.TOKEN_DATA.DATA,
        )
            .from(TokenData.TOKEN_DATA)
            .where(TokenData.TOKEN_DATA.HOLDER.eq(userAddress))
            .map {
                TokenDataRecord(
                    null,
                    it.get(TokenData.TOKEN_DATA.HOLDER),
                    it.get(TokenData.TOKEN_DATA.TOKEN_ID),
                    it.get(TokenData.TOKEN_DATA.NFT_TYPE),
                    it.get(TokenData.TOKEN_DATA.TOKEN_URI),
                    it.get(TokenData.TOKEN_DATA.KEYS),
                    it.get(TokenData.TOKEN_DATA.SETTINGS),
                    it.get(TokenData.TOKEN_DATA.SETTINGS_HASH),
                    it.get(TokenData.TOKEN_DATA.PROVIDER),
                    it.get(TokenData.TOKEN_DATA.DATA),
                )
            }
}
