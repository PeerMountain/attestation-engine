package com.kyc3.oracle.api.router.anonymous

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAnonymousListener
import com.kyc3.oracle.service.UserLoginChallengeService
import com.kyc3.oracle.user.Register
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class InitiateUserXMPPRegistrationListener(
    private val userLoginChallengeService: UserLoginChallengeService
) :
    OracleAnonymousListener<Register.InitiateUserXMPPRegistrationRequest, Register.InitiateUserXMPPRegistrationResponse> {
    override fun type(): Class<Register.InitiateUserXMPPRegistrationRequest> =
        Register.InitiateUserXMPPRegistrationRequest::class.java

    override fun acceptSync(
        event: Message.SignedAnonymousMessage,
        chat: Chat
    ): Register.InitiateUserXMPPRegistrationResponse? =
        event.message.unpack(type())
            .let { request -> userLoginChallengeService.createUserLoginChallenge(request.address) }
            ?.let {
                Register.InitiateUserXMPPRegistrationResponse.newBuilder()
                    .setAddress(it.address)
                    .setChallenge(it.challenge)
                    .build()
            }
}
