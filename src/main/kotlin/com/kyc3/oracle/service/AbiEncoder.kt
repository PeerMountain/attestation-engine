package com.kyc3.oracle.service

import com.kyc3.oracle.model.AttestationEngineEncodeNftRequest
import com.kyc3.oracle.nft.Nft
import org.springframework.stereotype.Service
import org.web3j.abi.DefaultFunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.generated.Bytes2
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric

@Service
class AbiEncoder(
    private val functionEncoder: DefaultFunctionEncoder
) {

    fun encodeNftSettings(nft: Nft.NftSettings): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(nft.attestationProvider),
                Uint256(nft.price.toLong()),
                hexToByte(Integer.toHexString(nft.type).padStart(4, '0')),
                Uint256(nft.expiration),
            )
        )

    fun engineEncodeNftSettings(request: AttestationEngineEncodeNftRequest): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(request.address),
                DynamicBytes(Numeric.hexStringToByteArray(request.encodedSettings)),
                DynamicBytes(Numeric.hexStringToByteArray(request.signedSettings)),
            )
        )

    fun encodeProofOfWork(address: String, nonce: Long): String =
        functionEncoder.encodeParameters(
            listOf(
                Address(address),
                Uint256(nonce)
            )
        )

    fun hexToByte(hexString: String): Bytes2 {
        return Bytes2(
            byteArrayOf(
                byteFromTwoChars(hexString[0], hexString[1]),
                byteFromTwoChars(hexString[2], hexString[3]),
            ),
        )
    }

    fun byteFromTwoChars(a: Char, b: Char): Byte {
        val firstDigit = toDigit(a)
        val secondDigit = toDigit(b)
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }
}
