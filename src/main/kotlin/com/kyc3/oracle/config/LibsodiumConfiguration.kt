package com.kyc3.oracle.config

import com.kyc3.oracle.config.properties.LibsodiumProperties
import com.kyc3.oracle.config.properties.WalletProperties
import com.kyc3.oracle.model.LibsodiumPublicKey
import com.muquit.libsodiumjna.SodiumLibrary
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.utils.Numeric
import java.util.*

@Configuration
class LibsodiumConfiguration(
  libsodiumProperties: LibsodiumProperties,
  private val walletProperties: WalletProperties,
  private val base64Encoder: Base64.Encoder
) {

  init {
    SodiumLibrary.setLibraryPath(libsodiumProperties.path)
  }

  @Bean
  fun libsodiumPublicKey() =
    LibsodiumPublicKey(
      base64Encoder.encodeToString(
        SodiumLibrary.cryptoPublicKey(
          Numeric.hexStringToByteArray(walletProperties.privateKey)
        )
      )
    )
}