package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class DataForAttestationRequestListener(
  private val oracleAPIResponse: OracleAPIResponse,
  private val attestationDataService: AttestationDataService
) : OracleListener<AttestationProviderOuterClass.DataForAttestationRequest> {

  override fun type(): Class<AttestationProviderOuterClass.DataForAttestationRequest> =
    AttestationProviderOuterClass.DataForAttestationRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type())
      .let { attestationDataService.findDataForAttestation(it.apAddress) }
      .let { list ->
        list.map {
          AttestationProviderOuterClass.AttestationData.newBuilder()
            .setId(it.id)
            .setCustomerAddress(it.customerAddress)
            .setData(it.data)
            .setHashKeyArray(it.hashKeyArray)
            .setTokenUri(it.tokenUri)
            .setHashedData(it.hashedData)
            .build()
        }
      }
      .let {
        oracleAPIResponse.responseToClient(
          chat,
          AttestationProviderOuterClass.DataForAttestationResponse.newBuilder()
            .addAllList(it)
            .build()
        )
      }
  }
}