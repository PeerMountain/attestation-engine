package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.attestation.AttestationDataOuterClass
import com.kyc3.oracle.ap.RequestAttestationData
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class DataForAttestationRequestListener(
  private val attestationDataService: AttestationDataService
) :
  OracleListener<RequestAttestationData.DataForAttestationRequest, RequestAttestationData.DataForAttestationResponse> {

  override fun type(): Class<RequestAttestationData.DataForAttestationRequest> =
    RequestAttestationData.DataForAttestationRequest::class.java

  override fun accept(event: Any, chat: Chat): RequestAttestationData.DataForAttestationResponse =
    event.unpack(type())
      .let { attestationDataService.findDataForAttestation(it.apAddress) }
      .let { list ->
        list.map {
          AttestationDataOuterClass.AttestationData.newBuilder()
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
        RequestAttestationData.DataForAttestationResponse.newBuilder()
          .addAllList(it)
          .build()
      }
}