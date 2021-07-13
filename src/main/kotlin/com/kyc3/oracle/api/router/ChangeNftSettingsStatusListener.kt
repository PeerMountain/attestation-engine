package com.kyc3.oracle.api.router

import com.google.protobuf.Any
import com.kyc3.oracle.ap.ChangeNftStatus
import com.kyc3.oracle.service.NftSettingsService
import org.jivesoftware.smack.chat2.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ChangeNftSettingsStatusListener(
  private val nftSettingsService: NftSettingsService,
) :
  OracleListener<ChangeNftStatus.ChangeNftSettingsStatusRequest, ChangeNftStatus.ChangeNftSettingsStatusResponse> {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun type(): Class<ChangeNftStatus.ChangeNftSettingsStatusRequest> =
    ChangeNftStatus.ChangeNftSettingsStatusRequest::class.java

  override fun accept(event: Any, chat: Chat): ChangeNftStatus.ChangeNftSettingsStatusResponse =
    nftSettingsService.changeNftStatus(event.unpack(type()))
      .takeIf { it }
      ?.let {
        ChangeNftStatus.ChangeNftSettingsStatusResponse.newBuilder()
          .build()
      }
      .also {
        if (it == null) {
          log.info("process='ChangeNftSettingsStatusListener' message='status can't be changed'")
        }
      }
      ?: throw IllegalArgumentException("status can't be changed")
}