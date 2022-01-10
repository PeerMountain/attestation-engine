package com.kyc3.oracle.api.router.anonymous

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAnonymousListener
import com.kyc3.oracle.service.UserLoginChallengeService
import com.kyc3.oracle.user.LoginChallenge
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class LoginChallengeListener(
    private val userLoginChallengeService: UserLoginChallengeService
) : OracleAnonymousListener<LoginChallenge.LoginChallengeRequest, LoginChallenge.LoginChallengeResponse> {

    override fun type(): Class<LoginChallenge.LoginChallengeRequest> =
        LoginChallenge.LoginChallengeRequest::class.java

    override fun acceptSync(event: Message.SignedAnonymousMessage, chat: Chat): LoginChallenge.LoginChallengeResponse? =
        event.message.unpack(type()).address.let { address ->
            userLoginChallengeService.userLoginChallengeByAddress(address).let {
                LoginChallenge.LoginChallengeResponse.newBuilder()
                    .setChallenge(it?.challenge ?: "")
                    .setAddress(address)
                    .setRegistered(it?.passwordInitiated?.let { true } ?: false)
                    .build()
            }
        }
}
