package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.model.AttestationDataDto
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service

@Service
class SubmitAttestationDataListener(
  private val oracleAPIResponse: OracleAPIResponse,
  private val attestationDataService: AttestationDataService
) :
  OracleListener<AttestationProviderOuterClass.SubmitAttestationData> {
  override fun type(): Class<AttestationProviderOuterClass.SubmitAttestationData> =
    AttestationProviderOuterClass.SubmitAttestationData::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type())
      .let {
        attestationDataService.submitAttestationData(
          AttestationDataDto(
            nftType = it.nftType,
            customerAddress = it.customerAddress,
            data = it.data,
            hashKeyArray = "0x10000006C350000022828531e543c61788be00d3ee000000000735233B600000",
            tokenUri = "https://short.ly/abc",
            hashedData = "0x6C3522828735233B60531e543c61788be00d3ee1031e543c61735233B6000000"
          )
        )
      }
  }
}