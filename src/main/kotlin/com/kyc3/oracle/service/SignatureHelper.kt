package com.kyc3.oracle.service

import org.web3j.crypto.Sign
import org.web3j.crypto.Sign.SignatureData
import org.web3j.utils.Numeric


class SignatureHelper {
  companion object {

    fun fromString(signature: String): Sign.SignatureData =
      signature.substring(2).decodeHex()
        .let { signatureBytes ->
          SignatureData(
            signatureBytes[64],
            signatureBytes.copyOfRange(0, 32),
            signatureBytes.copyOfRange(32, 64)
          )
        }

    fun toString(signature: SignatureData): String =
      (signature.r + signature.s + signature.v)
        .let { "0x${Numeric.toHexStringNoPrefix(it)}" }

    private fun String.decodeHex(): ByteArray {
      require(length % 2 == 0) { "Must have an even length" }

      return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
    }
  }
}