package com.kyc3.oracle.config

import com.kyc3.CashierContractV2
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteFunctionCall
import java.math.BigInteger

@TestConfiguration
class CashierContractV2TestConfig {

    @Bean
    @Primary
    fun cashierContractV3(web3j: Web3j, credentials: Credentials): CashierContractV2 =
        mock(CashierContractV2::class.java)
            .also {
                Mockito.`when`(it.proofOfWork).thenReturn(
                    RemoteFunctionCall(null) {
                        BigInteger.valueOf(2)
                    }
                )
            }
}