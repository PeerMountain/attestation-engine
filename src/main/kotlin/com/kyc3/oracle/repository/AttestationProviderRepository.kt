package com.kyc3.oracle.repository

import com.kyc3.oracle.types.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AttestationProviderRepository(
    private val dsl: DSLContext
) {

  fun create(
      name: String,
      address: String,
      transaction: String
  ): Int =
      dsl.insertInto(Tables.ATTESTATION_PROVIDER)
          .columns(
              Tables.ATTESTATION_PROVIDER.NAME,
              Tables.ATTESTATION_PROVIDER.ADDRESS,
              Tables.ATTESTATION_PROVIDER.INITIAL_TRANSACTION,
              Tables.ATTESTATION_PROVIDER.STATUS
          )
          .values(
              name,
              address,
              transaction,
              "IN_PROGRESS"
          )
          .execute()
}