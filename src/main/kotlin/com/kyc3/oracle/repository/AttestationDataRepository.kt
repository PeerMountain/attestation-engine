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
        Tables.ATTESTATION_DATA.DATA,
        Tables.ATTESTATION_DATA.HASH_KEY_ARRAY,
        Tables.ATTESTATION_DATA.TOKEN_URI,
        Tables.ATTESTATION_DATA.HASHED_DATA,
      )
      .values(
        record.providerId,
        record.data,
        record.hashKeyArray,
        record.tokenUri,
        record.hashedData
      )
      .execute()
}