package com.kyc3.oracle.repository

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
                TokenData.TOKEN_DATA.TOKEN_URI,
                TokenData.TOKEN_DATA.KEYS,
                TokenData.TOKEN_DATA.SETTINGS,
                TokenData.TOKEN_DATA.PROVIDER,
                TokenData.TOKEN_DATA.DATA
            )
            .values(
                tokenDataRecord.holder,
                tokenDataRecord.tokenId,
                tokenDataRecord.tokenUri,
                tokenDataRecord.keys,
                tokenDataRecord.settings,
                tokenDataRecord.provider,
                tokenDataRecord.data,
            )
            .execute()

    fun findAllByHolder(userAddress: String): List<TokenDataRecord> =
        dsl.select(
            TokenData.TOKEN_DATA.HOLDER,
            TokenData.TOKEN_DATA.TOKEN_ID,
            TokenData.TOKEN_DATA.TOKEN_URI,
            TokenData.TOKEN_DATA.KEYS,
            TokenData.TOKEN_DATA.SETTINGS,
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
                    it.get(TokenData.TOKEN_DATA.TOKEN_URI),
                    it.get(TokenData.TOKEN_DATA.KEYS),
                    it.get(TokenData.TOKEN_DATA.SETTINGS),
                    it.get(TokenData.TOKEN_DATA.PROVIDER),
                    it.get(TokenData.TOKEN_DATA.DATA),
                )
            }
}
