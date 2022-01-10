package com.kyc3.oracle.service

import com.kyc3.oracle.nft.TokenOuterClass
import com.kyc3.oracle.repository.TokenDataRepository
import com.kyc3.oracle.service.contract.CashierContractService
import com.kyc3.oracle.service.contract.TrustContractService
import com.kyc3.oracle.types.tables.records.TokenDataRecord
import com.kyc3.oracle.user.NftMint
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class NftMintService(
    private val cashierContractService: CashierContractService,
    private val trustContractService: TrustContractService,
    private val tokenDataRepository: TokenDataRepository,
    private val abiDecoder: AbiDecoder,
) {
    fun nftMint(
        apAddress: String,
        request: NftMint.NftMintRequest
    ): CompletableFuture<NftMint.NftMintResponse> =
        cashierContractService.nftMint(apAddress, request)
            .thenCompose { transactionReceipt ->
                val mintedEvent = cashierContractService.getTokenMintedEvent(transactionReceipt)
                    ?: throw RuntimeException("unable to receive minted event")

                trustContractService.getTokenData(mintedEvent.tokenId.toLong())
                    .thenApply { tokenData ->
                        val mintRequest = abiDecoder.decodeMintRequest(request.message)
                        val nftType = mintRequest
                            .let { abiDecoder.decodeNftSettings(it.encodedNftSettings) }.type

                        tokenDataRepository.insertTokenData(
                            TokenDataRecord(
                                null,
                                mintedEvent.holder,
                                mintedEvent.tokenId.toLong(),
                                nftType,
                                mintedEvent.tokenURI,
                                tokenData.keys,
                                mintRequest.encodedNftSettings,
                                tokenData.settings,
                                tokenData.provider,
                                tokenData.data,
                            )
                        )

                        NftMint.NftMintResponse.newBuilder()
                            .setNftMintTransactionHash(transactionReceipt.transactionHash)
                            .setToken(
                                TokenOuterClass.Token.newBuilder()
                                    .setHolder(mintedEvent.holder)
                                    .setTokenId(mintedEvent.tokenId.toLong())
                                    .setNftType(nftType)
                                    .setTokenUri(mintedEvent.tokenURI)
                                    .setKeys(tokenData.keys)
                                    .setSettings(mintRequest.encodedNftSettings)
                                    .setSettingsHash(tokenData.settings)
                                    .setProvider(tokenData.provider)
                                    .setData(tokenData.data)
                            )
                            .build()
                    }
            }
}
