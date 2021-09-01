package com.kyc3.oracle.repository

import com.kyc3.oracle.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AttestationDataRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var attestationDataRepository: AttestationDataRepository

    @Test
    fun `initial test`() {
    }
}
