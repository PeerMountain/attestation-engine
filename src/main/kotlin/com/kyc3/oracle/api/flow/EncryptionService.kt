package com.kyc3.oracle.api.flow

import com.kyc3.Message
import com.kyc3.oracle.config.properties.WalletProperties
import com.kyc3.oracle.model.EthEncryptedData
import com.muquit.libsodiumjna.SodiumLibrary
import org.springframework.stereotype.Component
import org.web3j.utils.Numeric
import java.util.*

@Component
class EncryptionService(
  private val base64Decoder: Base64.Decoder,
  private val base64Encoder: Base64.Encoder,
  private val walletProperties: WalletProperties
) {

  fun decryptMessage(encryptedMessage: Message.EncryptedMessage): ByteArray =
    SodiumLibrary.cryptoBoxOpenEasy(
      base64Decoder.decode(encryptedMessage.cipherText),
      base64Decoder.decode(encryptedMessage.nonce),
      base64Decoder.decode(encryptedMessage.ephemPublicKey),
      Numeric.hexStringToByteArray(walletProperties.privateKey)
    )

  fun encryptMessage(receiverPublicKey: String, data: String): EthEncryptedData {
    val ephemPublicKey = SodiumLibrary.cryptoBoxKeyPair()
    val pubKeyUInt8Array = base64Decoder.decode(receiverPublicKey)
    val nonce = SodiumLibrary.randomBytes(SodiumLibrary.cryptoBoxNonceBytes().toInt())

    val cipherText = SodiumLibrary.cryptoBoxEasy(
      data.toByteArray(),
      nonce,
      pubKeyUInt8Array,
      ephemPublicKey.privateKey
    )

    return EthEncryptedData(
      version = "x25519-xsalsa20-poly1305",
      nonce = base64Encoder.encodeToString(nonce),
      ephemPublicKey = base64Encoder.encodeToString(ephemPublicKey.publicKey),
      cipherText = base64Encoder.encodeToString(cipherText)
    )
  }
}