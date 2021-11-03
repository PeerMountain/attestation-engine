package com.kyc3.oracle.api.router.anonymous

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAnonymousListener
import com.kyc3.oracle.service.XMPPService
import com.kyc3.oracle.user.Register
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class RegisterUserListener(
    private val xmppService: XMPPService
) : OracleAnonymousListener<Register.RegisterUserRequest, Register.RegisterUserResponse> {
    override fun type(): Class<Register.RegisterUserRequest> =
        Register.RegisterUserRequest::class.java

    override fun accept(event: Message.SignedAnonymousMessage, chat: Chat): Register.RegisterUserResponse? =
        event.message.unpack(type()).let { request ->
            xmppService.createAccount(request.address, request.signedChallenge)
                .let {
                    Register.RegisterUserResponse.newBuilder()
                        .setAddress(request.address)
                        .build()
                }
        }
}
