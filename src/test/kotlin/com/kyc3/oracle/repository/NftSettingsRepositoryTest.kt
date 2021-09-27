package com.kyc3.oracle.repository

import com.kyc3.oracle.AbstractIntegrationTest
import com.kyc3.oracle.types.tables.records.AttestationProviderRecord
import com.kyc3.oracle.types.tables.records.NftSettingsRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

internal class NftSettingsRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var nftSettingsRepository: NftSettingsRepository

    @Autowired
    private lateinit var attestationProviderRepository: AttestationProviderRepository

    @Test
    fun `should create nft settings`() {
        val provider = createAttestationProviderWithName("NftSettingsRepositoryTest.provider1")!!

        val execute = nftSettingsRepository.createNft(
            NftSettingsRecord(
                null,
                provider.id,
                1,
                true,
                100,
                LocalDateTime.now(),
                "attestation_provider",
                "attestation_engine",
                "signed",
                true
            )
        )

        assertThat(execute).isEqualTo(1)
    }

    private fun createAttestationProviderWithName(name: String): AttestationProviderRecord? {
        val address = "address $name"
        attestationProviderRepository.create(
            name,
            address,
            "transaction $name",
        )

        return attestationProviderRepository.findByAddress(address)
    }
}
