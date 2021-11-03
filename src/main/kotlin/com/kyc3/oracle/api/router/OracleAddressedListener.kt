package com.kyc3.oracle.api.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message

interface OracleAddressedListener<Request : GeneratedMessageV3, Response : GeneratedMessageV3> :
    OracleListener<Message.SignedAddressedMessage, Request, Response>
