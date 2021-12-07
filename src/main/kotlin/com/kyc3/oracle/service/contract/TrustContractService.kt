package com.kyc3.oracle.service.contract

import com.kyc3.TrustContract
import com.kyc3.oracle.service.contract.model.ContractTokenDataDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.web3j.utils.Numeric
import java.math.BigInteger

@Service
class TrustContractService(
    private val trustContract: TrustContract
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getTokenData(tokenId: Long): ContractTokenDataDto =
        trustContract.getTokenData(BigInteger.valueOf(tokenId))
            .send()
            .let {
                ContractTokenDataDto(
                    Numeric.toHexString(it.component1()),
                    Numeric.toHexString(it.component2()),
                    it.component3(),
                    Numeric.toHexString(it.component4())
                )
            }
            .also {
                log.info("process=CashierContractV2:nftMint tokenId=$tokenId provider=${it.provider} receipt=$it")
            }

}