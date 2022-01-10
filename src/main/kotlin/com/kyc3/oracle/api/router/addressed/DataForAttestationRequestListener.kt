package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.ap.RequestAttestationData
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.attestation.AttestationDataOuterClass
import com.kyc3.oracle.service.AttestationDataService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class DataForAttestationRequestListener(
    private val attestationDataService: AttestationDataService
) :
    OracleAddressedListener<RequestAttestationData.DataForAttestationRequest, RequestAttestationData.DataForAttestationResponse> {

    override fun type(): Class<RequestAttestationData.DataForAttestationRequest> =
        RequestAttestationData.DataForAttestationRequest::class.java

    override fun acceptSync(event: Message.SignedAddressedMessage, chat: Chat): RequestAttestationData.DataForAttestationResponse =
        event.message.unpack(type())
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
                        .setNftType(it.nftType)
                        .build()
                }
            }
            .let {
                RequestAttestationData.DataForAttestationResponse.newBuilder()
                    .addAllList(it)
                    .build()
            }
}
