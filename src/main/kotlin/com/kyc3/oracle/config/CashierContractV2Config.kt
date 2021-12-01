package com.kyc3.oracle.config

import com.kyc3.CashierContractV2
import com.kyc3.oracle.Constants
import com.kyc3.oracle.config.properties.ContractsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

@Configuration
class CashierContractV2Config(
    private val contractsProperties: ContractsProperties
) {

    @Bean
    fun cashierContractV3(web3j: Web3j, credentials: Credentials): CashierContractV2 =
        CashierContractV2.load(
            contractsProperties.cashier, web3j, credentials,
            StaticGasProvider(
                BigInteger.valueOf(65_164_000),
                BigInteger.valueOf(6_800_000),
            )
        )
}
