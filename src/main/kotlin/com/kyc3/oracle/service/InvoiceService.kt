package com.kyc3.oracle.service

import com.kyc3.ap.challenge.GenerateChallenge
import com.kyc3.oracle.config.properties.ContractsProperties
import com.kyc3.oracle.payment.InvoiceOuterClass
import com.kyc3.oracle.repository.NftSettingsRepository
import com.kyc3.oracle.user.InitiateNftPurchase
import org.springframework.stereotype.Service

@Service
class InvoiceService(
    private val nftSettingsRepository: NftSettingsRepository,
    private val contractsProperties: ContractsProperties,
    private val nonceService: NonceService
) {

    fun generateInvoice(
        attestationProvider: String,
        dto: GenerateChallenge.GenerateChallengeResponse
    ): InitiateNftPurchase.InitiateNFTPurchaseResponse? =
        nftSettingsRepository.findByNftType(dto.nftType)
            ?.let {
                InitiateNftPurchase.InitiateNFTPurchaseResponse.newBuilder()
                    .setUserAddress(dto.userAddress)
                    .setNftType(dto.nftType)
                    .setChallenge(dto.challenge.data)
                    .setInvoice(
                        InvoiceOuterClass.Invoice.newBuilder()
                            .setAttestationProvider(attestationProvider)
                            .setPrice(it.price)
                            .setPaymentNonce(nonceService.nextNonce().toLong())
                            .setCashierAddress(contractsProperties.cashier)
                    )
                    .build()
            }
}
