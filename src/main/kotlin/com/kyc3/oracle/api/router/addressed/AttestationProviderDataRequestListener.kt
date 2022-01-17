package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.ap.AttestationProviderOuterClass
import com.kyc3.oracle.ap.Data
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class AttestationProviderDataRequestListener(
    private val attestationProviderService: AttestationProviderService
) :
    OracleAddressedListener<Data.AttestationProviderDataRequest, Data.AttestationProviderDataResponse> {

    override fun type(): Class<Data.AttestationProviderDataRequest> =
        Data.AttestationProviderDataRequest::class.java

    override fun acceptSync(event: Message.SignedAddressedMessage, chat: Chat): Data.AttestationProviderDataResponse =
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
            ?: Data.AttestationProviderDataResponse.newBuilder()
                .build()
}
