package com.kyc3.oracle.service

import com.kyc3.oracle.api.UserKeysStorage
import com.kyc3.oracle.model.UserKeys
import org.ehcache.Cache
import org.springframework.stereotype.Service

@Service
class UserKeysService(
    private val cache: Cache<String, UserKeys>
) : UserKeysStorage {

    override fun store(address: String, userKeys: UserKeys): Unit =
        cache.put(address.lowercase(), userKeys)

    override fun getUserKeys(address: String): UserKeys? =
        cache.get(address.lowercase())
}
