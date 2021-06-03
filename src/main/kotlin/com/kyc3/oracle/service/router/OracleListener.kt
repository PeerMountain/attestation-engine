package com.kyc3.oracle.service.router

import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3

interface OracleListener<T : GeneratedMessageV3> {
  fun type(): Class<T>

  fun accept(event: Any)
}