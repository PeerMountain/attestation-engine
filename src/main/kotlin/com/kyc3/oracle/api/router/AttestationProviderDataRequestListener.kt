package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.api.OracleAPIResponse
import com.kyc3.oracle.attestation.AttestationProviderOuterClass
import com.kyc3.oracle.service.AttestationProviderService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttestationProviderDataRequestListener(
    private val oracleAPIResponse: OracleAPIResponse,
    private val attestationProviderService: AttestationProviderService
) :
    OracleListener<AttestationProviderOuterClass.AttestationProviderDataRequest> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun type(): Class<AttestationProviderOuterClass.AttestationProviderDataRequest> =
      AttestationProviderOuterClass.AttestationProviderDataRequest::class.java

  override fun accept(event: Any, chat: Chat) {
    event.unpack(type()).address
        .let { attestationProviderService.getProviderByAddress(it) }
        ?.let {
          AttestationProviderOuterClass.AttestationProvider.newBuilder()
              .setAddress(it.address)
              .setName(it.name)
              .build()
        }
        ?.let { oracleAPIResponse.responseToClient(chat, it) }
        ?: run {
          log.info("process='AttestationProviderDataRequestListener' message='can't find provider by address'")
        }
  }
}