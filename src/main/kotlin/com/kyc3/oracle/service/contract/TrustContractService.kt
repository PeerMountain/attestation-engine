package com.kyc3.oracle.service.contract

import com.kyc3.TrustContract
import com.kyc3.oracle.service.contract.model.ContractTokenDataDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.util.concurrent.CompletableFuture

@Service
class TrustContractService(
    private val trustContract: TrustContract
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getTokenData(tokenId: Long): CompletableFuture<ContractTokenDataDto> =
        trustContract.getTokenData(BigInteger.valueOf(tokenId))
            .sendAsync()
            .thenApply {
                ContractTokenDataDto(
                    Numeric.toHexString(it.component1()),
                    Numeric.toHexString(it.component2()),
                    it.component3(),
                    Numeric.toHexString(it.component4())
                )
            }
            .whenComplete { receipt, ex ->
                if (ex == null) {
                    log.info("process=CashierContractV2:nftMint tokenId=$tokenId provider=${receipt.provider} receipt=$receipt")
                } else {
                    log.error("process=CashierContractV2:nftMint tokenId=$tokenId", ex)
                }
            }
}
