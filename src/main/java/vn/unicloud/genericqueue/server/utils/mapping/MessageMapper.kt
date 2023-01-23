package vn.unicloud.genericqueue.server.utils.mapping

import com.google.protobuf.ByteString
import vn.unicloud.genericqueue.protobuf.Message
import vn.unicloud.genericqueue.protobuf.ProducerMessage
import java.util.UUID

fun ProducerMessage.toMessage() = Message.newBuilder()
    .setId(id)
    .setPayload(payload)
    .setSchemaId(schemaId)
    .addAllHeaders(headersList)
    .setReplayId(ByteString.copyFrom(UUID.randomUUID().toBytes()))
    .build()!!