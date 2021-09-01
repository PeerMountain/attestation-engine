package com.kyc3.oracle.repository

import com.kyc3.oracle.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AttestationProviderRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var attestationProviderRepository: AttestationProviderRepository

    @Test
    fun `should create attestation provider`() {
        val address = "AttestationProviderRepositoryTest.address1"
        val name = "AttestationProviderRepositoryTest.provider"
        val transaction = "AttestationProviderRepositoryTest.transaction"
        val executed = attestationProviderRepository.create(
            name,
            address,
            transaction
        )

        assertThat(executed)
            .isEqualTo(1)

        val providerByAddress = attestationProviderRepository.findByAddress(address)

        assertThat(providerByAddress)
            .isNotNull()
            .hasFieldOrPropertyWithValue("name", name)
            .hasFieldOrPropertyWithValue("address", address)
            .hasFieldOrPropertyWithValue("initialTransaction", transaction)
            .hasFieldOrPropertyWithValue("status", "IN_PROGRESS")
    }

    @Test
    fun `should change status for provider`() {
        val provider = "AttestationProviderRepositoryTest.provider2"
        val address = "AttestationProviderRepositoryTest.address2"
        attestationProviderRepository.create(
            provider,
            address,
            "AttestationProviderRepositoryTest.transaction2"
        )

        val changeStatus = attestationProviderRepository.changeStatus(provider, address, "NEW_STATUS")

        assertThat(changeStatus).isTrue()
    }

    @Test
    fun `should not change status for provider when provider name is not correct`() {
        val provider = "AttestationProviderRepositoryTest.provider3"
        val address = "AttestationProviderRepositoryTest.address3"
        attestationProviderRepository.create(
            provider,
            address,
            "AttestationProviderRepositoryTest.transaction3"
        )

        val changeStatus = attestationProviderRepository.changeStatus("different name", address, "NEW_STATUS")

        assertThat(changeStatus).isFalse()
    }

    @Test
    fun `should not change status for provider when address is not correct`() {
        val provider = "AttestationProviderRepositoryTest.provider4"
        val address = "AttestationProviderRepositoryTest.address4"
        attestationProviderRepository.create(
            provider,
            address,
            "AttestationProviderRepositoryTest.transaction4"
        )

        val changeStatus = attestationProviderRepository.changeStatus(provider, "different address", "NEW_STATUS")

        assertThat(changeStatus).isFalse()
    }
}
