package com.kyc3.oracle.job

import com.kyc3.oracle.service.AttestationProviderService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class AttestationProviderVerificationJob(
    private val attestationProviderService: AttestationProviderService
) {

  @Scheduled(fixedDelay = 10_000)
  fun checkAttestationProviderTransactions() {
    attestationProviderService.findProvidersToProcess()
        .forEach {
          attestationProviderService.validateProviderRegistration(it)
        }
  }
}