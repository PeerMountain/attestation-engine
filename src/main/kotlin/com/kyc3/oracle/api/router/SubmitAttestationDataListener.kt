package com.kyc3.oracle.api.router

import com.kyc3.Message
import com.kyc3.oracle.ap.SignAttestation
import com.kyc3.oracle.model.AttestationDataDto
import com.kyc3.oracle.service.AttestationDataService
import com.kyc3.oracle.user.SubmitAttestation
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Service

@Service
class SubmitAttestationDataListener(
    private val attestationDataService: AttestationDataService
) :
    OracleListener<SubmitAttestation.SubmitAttestationDataRequest, SignAttestation.SignAttestationDataResponse> {
    override fun type(): Class<SubmitAttestation.SubmitAttestationDataRequest> =
        SubmitAttestation.SubmitAttestationDataRequest::class.java

    override fun accept(event: Message.SignedMessage, chat: Chat): SignAttestation.SignAttestationDataResponse =
        event.message.unpack(type())
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
            .let { SignAttestation.SignAttestationDataResponse.newBuilder().build() }
}
