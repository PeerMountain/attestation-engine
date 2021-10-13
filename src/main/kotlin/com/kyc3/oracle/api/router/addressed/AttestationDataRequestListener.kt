package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.attestation.AttestationDataOuterClass
import com.kyc3.oracle.service.AttestationDataService
import com.kyc3.oracle.user.RequestAttestationList
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class AttestationDataRequestListener(
    private val attestationDataService: AttestationDataService,
) :
    OracleAddressedListener<RequestAttestationList.AttestationDataListRequest, RequestAttestationList.AttestationDataListResponse> {

    override fun type(): Class<RequestAttestationList.AttestationDataListRequest> =
        RequestAttestationList.AttestationDataListRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): RequestAttestationList.AttestationDataListResponse =
        event.message.unpack(type())
            .let {
                attestationDataService.findUserAttestations(it.customerAddress)
            }
            .let {
                it.map { record ->
                    AttestationDataOuterClass.SignedAttestationData
                        .newBuilder()
                        .setAttestation(
                            AttestationDataOuterClass.AttestationData.newBuilder()
                                .setId(record.id)
                                .setNftType(record.nftType)
                                .setCustomerAddress(record.customerAddress)
                                .setData(record.data)
                                .setHashKeyArray(record.hashKeyArray)
                                .setTokenUri(record.tokenUri)
                                .setHashedData(record.hashedData)
                                .build()
                        )
                        .setSignedMessage(record.signedMessage)
                        .build()
                }
            }
            .let {
                RequestAttestationList.AttestationDataListResponse.newBuilder()
                    .addAllUserData(it)
                    .build()
            }
}
