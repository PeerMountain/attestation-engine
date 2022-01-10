package com.kyc3.oracle.api.router.addressed

import com.kyc3.Message
import com.kyc3.oracle.api.router.OracleAddressedListener
import com.kyc3.oracle.nft.TokenOuterClass
import com.kyc3.oracle.repository.TokenDataRepository
import com.kyc3.oracle.user.UserToken
import org.jivesoftware.smack.chat2.Chat
import org.springframework.stereotype.Component

@Component
class UserTokenListener(
    private val tokenDataRepository: TokenDataRepository
) : OracleAddressedListener<UserToken.UserTokenRequest, UserToken.UserTokenResponse> {
    override fun type(): Class<UserToken.UserTokenRequest> =
        UserToken.UserTokenRequest::class.java

    override fun acceptSync(event: Message.SignedAddressedMessage, chat: Chat): UserToken.UserTokenResponse =
        UserToken.UserTokenResponse.newBuilder()
            .addAllTokens(
                tokenDataRepository.findAllByHolder(
                    chat.xmppAddressOfChatPartner.localpart.asUnescapedString()
                )
                    .map {
                        TokenOuterClass.Token.newBuilder()
                            .setHolder(it.holder)
                            .setTokenId(it.tokenId.toLong())
                            .setNftType(it.nftType)
                            .setTokenUri(it.tokenUri)
                            .setKeys(it.keys)
                            .setSettings(it.settings)
                            .setSettingsHash(it.settingsHash)
                            .setProvider(it.provider)
                            .setData(it.data)
                            .build()
                    }
            )
            .build()
}
