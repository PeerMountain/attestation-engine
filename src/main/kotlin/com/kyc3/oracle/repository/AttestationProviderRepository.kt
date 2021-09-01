package com.kyc3.oracle.repository

import com.kyc3.oracle.types.Tables
import com.kyc3.oracle.types.tables.records.AttestationProviderRecord
import org.jooq.DSLContext
import org.jooq.Result
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

    fun findFirstWithStatus(limit: Long, status: String): Result<AttestationProviderRecord> =
        dsl.selectFrom(Tables.ATTESTATION_PROVIDER)
            .where(Tables.ATTESTATION_PROVIDER.STATUS.eq(status))
            .limit(limit)
            .fetch()

    fun changeStatus(name: String, address: String, status: String): Boolean =
        dsl.update(Tables.ATTESTATION_PROVIDER)
            .set(Tables.ATTESTATION_PROVIDER.STATUS, status)
            .where(Tables.ATTESTATION_PROVIDER.NAME.eq(name))
            .and(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(address))
            .execute() == 1

    fun findByAddress(address: String): AttestationProviderRecord? =
        dsl.selectFrom(Tables.ATTESTATION_PROVIDER)
            .where(Tables.ATTESTATION_PROVIDER.ADDRESS.eq(address))
            .fetchOne()
}
