package com.kyc3.oracle.service

import com.kyc3.oracle.nft.TokenOuterClass
import com.kyc3.oracle.repository.TokenDataRepository
import com.kyc3.oracle.service.contract.CashierContractService
import com.kyc3.oracle.service.contract.TrustContractService
import com.kyc3.oracle.types.tables.records.TokenDataRecord
import com.kyc3.oracle.user.NftMint
import org.springframework.stereotype.Service

@Service
class NftMintService(
    private val cashierContractService: CashierContractService,
    private val trustContractService: TrustContractService,
    private val tokenDataRepository: TokenDataRepository
) {
    fun nftMint(
        apAddress: String,
        request: NftMint.NftMintRequest
    ): NftMint.NftMintResponse {
        val transactionReceipt = cashierContractService.nftMint(apAddress, request)

        val mintedEvent = cashierContractService.getTokenMintedEvent(transactionReceipt)
            ?: throw RuntimeException("unable to receive minted event")

        val tokenData = trustContractService.getTokenData(mintedEvent.tokenId.toLong())

        tokenDataRepository.insertTokenData(
            TokenDataRecord(
                null,
                mintedEvent.holder,
                mintedEvent.tokenId.toLong(),
                mintedEvent.tokenURI,
                tokenData.keys,
                tokenData.settings,
                tokenData.provider,
                tokenData.data,
            )
        )

        return NftMint.NftMintResponse.newBuilder()
            .setNftMintTransactionHash(transactionReceipt.transactionHash)
            .setToken(
                TokenOuterClass.Token.newBuilder()
                    .setHolder(mintedEvent.holder)
                    .setTokenId(mintedEvent.tokenId.toLong())
                    .setTokenUri(mintedEvent.tokenURI)
                    .setKeys(tokenData.keys)
                    .setSettings(tokenData.settings)
                    .setProvider(tokenData.provider)
                    .setData(tokenData.data)
            )
            .build()
    }
}