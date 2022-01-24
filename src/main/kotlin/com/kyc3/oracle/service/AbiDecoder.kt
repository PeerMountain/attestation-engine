package com.kyc3.oracle.service

import com.kyc3.oracle.model.DecodedNftSettings
import com.kyc3.oracle.model.MintRequest
import com.kyc3.oracle.model.TransferRequest
import org.springframework.stereotype.Service
import org.web3j.abi.DefaultFunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes2
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric
import java.math.BigInteger

@Service
class AbiDecoder(
    private val functionDecoder: DefaultFunctionReturnDecoder
) {

    fun decodeMintRequest(encodedRequest: String): MintRequest =
        functionDecoder.decodeFunctionResult(
            encodedRequest,
            listOf(
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<DynamicBytes>() {},
                object : TypeReference<DynamicBytes>() {},
                object : TypeReference<DynamicBytes>() {},
                object : TypeReference<DynamicBytes>() {},
            ) as List<TypeReference<Type<Any>>>
        )
            .let {
                MintRequest(
                    it[0].value as String,
                    it[1].value as BigInteger,
                    Numeric.toHexString(it[2].value as ByteArray),
                    Numeric.toHexString(it[3].value as ByteArray),
                    Numeric.toHexString(it[4].value as ByteArray),
                    Numeric.toHexString(it[5].value as ByteArray),
                )
            }

    fun decodeTransferRequest(encodedRequest: String): TransferRequest =
        functionDecoder.decodeFunctionResult(
            encodedRequest,
            listOf(
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Address>() {},
            ) as List<TypeReference<Type<Any>>>
        )
            .let {
                TransferRequest(
                    address = it[0].value as String,
                    tokenId = it[1].value.let { value -> value as BigInteger }.toLong(),
                    nonce = it[2].value.let { value -> value as BigInteger },
                    cashierAddress = it[3].value as String,
                )
            }

    fun decodeNftSettings(encodedRequest: String): DecodedNftSettings =
        functionDecoder.decodeFunctionResult(
            encodedRequest,
            listOf(
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Bytes2>() {},
                object : TypeReference<Uint256>() {},
            ) as List<TypeReference<Type<Any>>>
        )
            .let {
                DecodedNftSettings(
                    it[0].value as String,
                    (it[1].value as BigInteger).toLong(),
                    Numeric.toHexStringNoPrefix(it[2].value as ByteArray).toLong(16),
                    (it[3].value as BigInteger).toLong(),
                )
            }
}
