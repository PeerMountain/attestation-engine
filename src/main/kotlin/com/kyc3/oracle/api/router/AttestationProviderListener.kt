package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.attestation.AttestationProvider
import com.kyc3.oracle.service.AttestationProviderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AttestationProviderListener(
    private val attestationProviderService: AttestationProviderService
) : OracleListener<AttestationProvider.AttestationProviderRegister> {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun type(): Class<AttestationProvider.AttestationProviderRegister> =
      AttestationProvider.AttestationProviderRegister::class.java

  override fun accept(event: Any) {
    event.unpack(type())
        .also {
          log.info("process='AttestationProviderListener' message='received message' event='${it}'")
          attestationProviderService.create(it.name, it.transaction)
        }
  }
}