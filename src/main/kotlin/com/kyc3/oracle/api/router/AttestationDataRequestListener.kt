package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class AttestationDataRequestListener(
  private val attestationDataService: AttestationDataService,
  private val oracleAPIResponse: OracleAPIResponse
) : OracleListener<AttestationProviderOuterClass.AttestationDataRequest> {
  override fun type(): Class<AttestationProviderOuterClass.AttestationDataRequest> =
    AttestationProviderOuterClass.AttestationDataRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type())
      .let {
        attestationDataService.findUserAttestations(it.customerAddress)
      }
      .let {
        it.map { record ->
          AttestationProviderOuterClass.SignedAttestationData
            .newBuilder()
            .setAttestation(
              AttestationProviderOuterClass.AttestationData.newBuilder()
                .setId(record.id)
                .setCustomerAddress(record.customerAddress)
                .setData(record.data)
                .setHashKeyArray(record.hashKeyArray)
                .setTokenUri(record.tokenUri)
                .setHashedData(record.hashedData)
                .build()
            )
            .setSignedMessage(record.signedData)
            .build()
        }
      }
      .let {
        AttestationProviderOuterClass.AttestationDataResponse.newBuilder()
          .addAllUserData(it)
          .build()
      }
      .let { oracleAPIResponse.responseToClient(chat, it) }
  }
}