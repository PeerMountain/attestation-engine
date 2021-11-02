package com.kyc3.oracle.api.router

import com.google.protobuf.GeneratedMessageV3
import com.kyc3.Message

interface OracleAnonymousListener<Request : GeneratedMessageV3, Response : GeneratedMessageV3> :
    OracleListener<Message.SignedAnonymousMessage, Request, Response>
