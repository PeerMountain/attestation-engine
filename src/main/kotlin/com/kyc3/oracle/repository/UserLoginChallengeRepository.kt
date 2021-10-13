package com.kyc3.oracle.repository

import com.kyc3.oracle.types.tables.UserLoginChallenge
import com.kyc3.oracle.types.tables.records.UserLoginChallengeRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
@Transactional(propagation = Propagation.MANDATORY)
class UserLoginChallengeRepository(
    private val dslContext: DSLContext
) {

    fun findUserChallenge(address: String): UserLoginChallengeRecord? =
        dslContext.selectFrom(UserLoginChallenge.USER_LOGIN_CHALLENGE)
            .where(UserLoginChallenge.USER_LOGIN_CHALLENGE.ADDRESS.eq(address))
            .fetchOne()

    fun insertUserChallenge(address: String, challenge: String): UserLoginChallengeRecord? =
        dslContext.insertInto(UserLoginChallenge.USER_LOGIN_CHALLENGE)
            .columns(
                UserLoginChallenge.USER_LOGIN_CHALLENGE.ADDRESS,
                UserLoginChallenge.USER_LOGIN_CHALLENGE.CHALLENGE,
            )
            .values(address, challenge)
            .returning(UserLoginChallenge.USER_LOGIN_CHALLENGE.asterisk())
            .fetchOne()

    fun initiatePassword(address: String): Int =
        dslContext.update(UserLoginChallenge.USER_LOGIN_CHALLENGE)
            .set(
                UserLoginChallenge.USER_LOGIN_CHALLENGE.PASSWORD_INITIATED,
                LocalDateTime.now()
            )
            .where(UserLoginChallenge.USER_LOGIN_CHALLENGE.ADDRESS.eq(address))
            .execute()

}
