package com.kyc3.oracle.service

import com.kyc3.oracle.repository.UserLoginChallengeRepository
import com.kyc3.oracle.types.tables.records.UserLoginChallengeRecord
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserLoginChallengeService(
    private val userLoginChallengeRepository: UserLoginChallengeRepository
) {

    companion object {
        const val CHALLENGE_LENGTH = 10
    }

    @Transactional
    fun userLoginChallengeByAddress(address: String): UserLoginChallengeRecord? =
        userLoginChallengeRepository.findUserChallenge(address)

    @Transactional
    fun createUserLoginChallenge(address: String) =
        userLoginChallengeRepository.insertUserChallenge(
            address,
            RandomStringUtils.random(CHALLENGE_LENGTH, true, true)
        )
}
