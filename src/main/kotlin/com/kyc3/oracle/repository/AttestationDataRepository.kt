package com.kyc3.oracle.repository

import com.kyc3.oracle.model.EnrichedAttestationData
import com.kyc3.oracle.types.Tables
import com.kyc3.oracle.types.tables.records.AttestationDataRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AttestationDataRepository(
    private val dsl: DSLContext
) {

    fun create(record: AttestationDataRecord) =
        dsl.insertInto(Tables.ATTESTATION_DATA)
            .columns(
                Tables.ATTESTATION_DATA.NFT_ID,
                Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS,
                Tables.ATTESTATION_DATA.DATA,
                Tables.ATTESTATION_DATA.HASH_KEY_ARRAY,
                Tables.ATTESTATION_DATA.TOKEN_URI,
                Tables.ATTESTATION_DATA.HASHED_DATA,
            )
            .values(
                record.nftId,
                record.customerAddress,
                record.data,
                record.hashKeyArray,
                record.tokenUri,
                record.hashedData
            )
            .execute()

    fun findAllForProvider(apAddress: String): List<AttestationDataRecord> =
        dsl.select(Tables.ATTESTATION_DATA.asterisk())
            .from(Tables.ATTESTATION_PROVIDER)
            .join(Tables.NFT_SETTINGS)
            .on(Tables.NFT_SETTINGS.AP_ID.eq(Tables.ATTESTATION_PROVIDER.ID))
            .join(Tables.ATTESTATION_DATA)
            .on(Tables.ATTESTATION_DATA.NFT_ID.eq(Tables.NFT_SETTINGS.ID))
            .where(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(apAddress))
            .fetchInto(AttestationDataRecord::class.java)

    fun findByNftTypeId(nftTypeId: Long): List<AttestationDataRecord> =
        dsl.selectFrom(Tables.ATTESTATION_DATA)
            .where(Tables.ATTESTATION_DATA.NFT_ID.eq(nftTypeId))
            .toList()

    fun updateSignedMessage(id: Long, signedMessage: String): Int =
        dsl.update(Tables.ATTESTATION_DATA)
            .set(Tables.ATTESTATION_DATA.SIGNED_DATA, signedMessage)
            .where(Tables.ATTESTATION_DATA.ID.eq(id))
            .execute()

    fun findByCustomerAddress(customerAddress: String): List<EnrichedAttestationData> =
        dsl.select(
            Tables.ATTESTATION_DATA.ID,
            Tables.NFT_SETTINGS.TYPE,
            Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS,
            Tables.ATTESTATION_DATA.DATA,
            Tables.ATTESTATION_DATA.HASH_KEY_ARRAY,
            Tables.ATTESTATION_DATA.TOKEN_URI,
            Tables.ATTESTATION_DATA.HASHED_DATA,
            Tables.ATTESTATION_DATA.SIGNED_DATA,
        )
            .from(Tables.ATTESTATION_DATA)
            .join(Tables.NFT_SETTINGS)
            .on(Tables.ATTESTATION_DATA.NFT_ID.eq(Tables.NFT_SETTINGS.ID))
            .where(Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS.eq(customerAddress))
            .and(Tables.ATTESTATION_DATA.SIGNED_DATA.isNotNull)
            .toList()
            .map {
                EnrichedAttestationData(
                    id = it.get(Tables.ATTESTATION_DATA.ID),
                    nftType = it.get(Tables.NFT_SETTINGS.TYPE),
                    customerAddress = it.get(Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS),
                    data = it.get(Tables.ATTESTATION_DATA.DATA),
                    hashKeyArray = it.get(Tables.ATTESTATION_DATA.HASH_KEY_ARRAY),
                    tokenUri = it.get(Tables.ATTESTATION_DATA.TOKEN_URI),
                    hashedData = it.get(Tables.ATTESTATION_DATA.HASHED_DATA),
                    signedMessage = it.get(Tables.ATTESTATION_DATA.SIGNED_DATA)
                )
            }
}
