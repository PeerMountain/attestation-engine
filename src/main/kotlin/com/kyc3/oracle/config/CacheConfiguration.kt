package com.kyc3.oracle.config

import com.kyc3.oracle.model.UserKeys
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfiguration {

    @Bean
    fun cacheManger(): CacheManager =
        CacheManagerBuilder.newCacheManagerBuilder()
            .build()
            .also { it.init() }

    @Bean
    fun cache(cacheManager: CacheManager): Cache<String, UserKeys> =
        cacheManager.createCache(
            "PublicKeyCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.java,
                UserKeys::class.java,
                ResourcePoolsBuilder.heap(10),
            )
        )
}
