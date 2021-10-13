package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.ap.AttestationProviderOuterClass
import com.kyc3.oracle.ap.Data
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttestationProviderDataRequestListener(
    private val oracleAPIResponse: OracleAPIResponse,
    private val attestationProviderService: AttestationProviderService
) :
    OracleAddressedListener<Data.AttestationProviderDataRequest, Data.AttestationProviderDataResponse> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun type(): Class<Data.AttestationProviderDataRequest> =
        Data.AttestationProviderDataRequest::class.java

    override fun accept(event: Message.SignedAddressedMessage, chat: Chat): Data.AttestationProviderDataResponse =
        event.message.unpack(type()).address
            .let { attestationProviderService.getProviderByAddress(it) }
            ?.let {
                Data.AttestationProviderDataResponse.newBuilder()
                    .setProvider(
                        AttestationProviderOuterClass.AttestationProvider.newBuilder()
                            .setAddress(it.address)
                            .setName(it.name)
                            .build()
                    )
                    .build()
            }
            .also {
                if (it == null) {
                    log.info("process='AttestationProviderDataRequestListener' message='can't find provider by address'")
                }
            }
            ?: throw IllegalArgumentException("can't find provider by address")
}
