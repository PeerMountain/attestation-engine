package com.kyc3.oracle.repository

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
        Tables.ATTESTATION_DATA.PROVIDER_ID,
        Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS,
        Tables.ATTESTATION_DATA.DATA,
        Tables.ATTESTATION_DATA.HASH_KEY_ARRAY,
        Tables.ATTESTATION_DATA.TOKEN_URI,
        Tables.ATTESTATION_DATA.HASHED_DATA,
      )
      .values(
        record.providerId,
        record.customerAddress,
        record.data,
        record.hashKeyArray,
        record.tokenUri,
        record.hashedData
      )
      .execute()

  fun findByProviderId(providerId: Long) =
    dsl.selectFrom(Tables.ATTESTATION_DATA)
      .where(Tables.ATTESTATION_DATA.PROVIDER_ID.eq(providerId))
      .toList()

  fun updateSignedMessage(id: Long, signedMessage: String): Int =
    dsl.update(Tables.ATTESTATION_DATA)
      .set(Tables.ATTESTATION_DATA.SIGNED_DATA, signedMessage)
      .where(Tables.ATTESTATION_DATA.ID.eq(id))
      .execute()

  fun findByCustomerAddress(customerAddress: String): List<AttestationDataRecord> =
    dsl.selectFrom(Tables.ATTESTATION_DATA)
      .where(Tables.ATTESTATION_DATA.CUSTOMER_ADDRESS.eq(customerAddress))
      .toList()
}