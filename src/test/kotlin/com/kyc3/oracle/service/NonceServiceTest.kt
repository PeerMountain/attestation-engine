package com.kyc3.oracle.service

import com.kyc3.CashierContractV2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.RemoteFunctionCall
import java.math.BigInteger

internal class NonceServiceTest {
    private val credentials = Credentials.create("e4e1b0fe2cf2b6e55d0e2f3ea3dcf5e04527b4bef4b1b2c20fb7a9a864acd4d6")

    val cashierContractService = mock(CashierContractV2::class.java)

    private val nonceService = NonceService(
        cashierContractService,
        AbiEncoder(DefaultFunctionEncoder()),
        credentials
    )

    @Test
    fun `should generate oracle nonce`() {
        `when`(cashierContractService.proofOfWork).thenReturn(RemoteFunctionCall(null) {
            BigInteger.valueOf(2)
        })

        assertThat(nonceService.generateNonce()).isGreaterThan(0)
    }
}
