package com.kyc3.oracle.config

import com.kyc3.oracle.api.APIResponseService
import com.kyc3.oracle.api.EncryptionService
import com.kyc3.oracle.config.properties.WalletProperties
import com.kyc3.oracle.service.UserKeysService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64

@Configuration
class AttestationEngineConfiguration {

    @Bean
    fun apiResponseService(
        base64Decoder: Base64.Decoder,
        base64Encoder: Base64.Encoder,
        userKeysService: UserKeysService,
        walletProperties: WalletProperties,
    ): APIResponseService =
        APIResponseService(
            base64Encoder,
            base64Decoder,
            userKeysService,
            walletProperties.privateKey,
        )

    @Bean
    fun encryptionService(
        base64Decoder: Base64.Decoder,
        base64Encoder: Base64.Encoder,
        walletProperties: WalletProperties,
    ): EncryptionService =
        EncryptionService(
            base64Decoder,
            base64Encoder,
            walletProperties.privateKey,
        )
}
