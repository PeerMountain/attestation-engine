package com.kyc3.oracle.api.router.anonymous

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAnonymousListener
import com.kyc3.oracle.service.UserLoginChallengeService
import com.kyc3.oracle.service.Web3Service
import com.kyc3.oracle.service.XMPPService
import com.kyc3.oracle.user.Register
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class RegisterUserListener(
    private val xmppService: XMPPService,
    private val userLoginChallengeService: UserLoginChallengeService,
    private val web3Service: Web3Service
) : OracleAnonymousListener<Register.RegisterUserRequest, Register.RegisterUserResponse> {
    override fun type(): Class<Register.RegisterUserRequest> =
        Register.RegisterUserRequest::class.java

    override fun acceptSync(event: Message.SignedAnonymousMessage, chat: Chat): Register.RegisterUserResponse? =
        event.message.unpack(type()).let { request ->
            userLoginChallengeService.userLoginChallengeByAddress(request.address)
                ?.takeIf { web3Service.verifySignature(it.challenge, request.signedChallenge, request.address) }
                ?.let {
                    xmppService.createAccount(request.address, request.signedChallenge)
                        .let {
                            Register.RegisterUserResponse.newBuilder()
                                .setAddress(request.address)
                                .build()
                        }
                }
        }
}
