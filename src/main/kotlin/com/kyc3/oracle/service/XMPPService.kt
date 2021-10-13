package com.kyc3.oracle.service

import com.kyc3.oracle.config.properties.XmppProperties
import com.kyc3.oracle.repository.UserLoginChallengeRepository
import org.jivesoftware.smackx.admin.ServiceAdministrationManager
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Domainpart
import org.jxmpp.jid.parts.Localpart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class XMPPService(
    private val serviceAdministrationManager: ServiceAdministrationManager,
    private val xmppProperties: XmppProperties,
    private val userLoginChallengeRepository: UserLoginChallengeRepository
) {

    @Transactional
    fun createAccount(username: String, password: String) =
        serviceAdministrationManager.addUser(
            JidCreate.entityBareFrom(
                Localpart.from(username),
                Domainpart.from(xmppProperties.domain),
            ),
            password,
        )
            .also {
                userLoginChallengeRepository.initiatePassword(
                    username
                )
            }
}
