package com.kyc3.oracle.service

import com.kyc3.oracle.repository.Web3Repository
import org.springframework.stereotype.Service

@Service
class Web3Service(
    private val web3Repository: Web3Repository
) {

  fun isTransactionValid(transactionHash: String, providerAddress: String) =
      web3Repository.getTransactionReceipt(transactionHash)
          .transactionReceipt
          .map {
            it.from == providerAddress
                && it.blockNumber != null
                && it.isStatusOK
          }
          .stream()
          .allMatch { it }

}