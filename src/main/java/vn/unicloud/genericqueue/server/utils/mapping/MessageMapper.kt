package vn.unicloud.genericqueue.server.utils.mapping

import com.google.protobuf.ByteString
import vn.unicloud.genericequeue.persistence.entities.Header
import vn.unicloud.genericequeue.persistence.entities.Node
import vn.unicloud.genericqueue.protobuf.Message
import vn.unicloud.genericqueue.protobuf.MessageHeader
import vn.unicloud.genericqueue.protobuf.ProducerMessage
import java.util.UUID

fun ProducerMessage.toMessage() = Message.newBuilder()
    .setId(id)
    .setPayload(payload)
    .setSchemaId(schemaId)
    .addAllHeaders(headersList)
    .setReplayId(ByteString.copyFrom(UUID.randomUUID().toBytes()))
    .build()!!

fun MessageHeader.toHeader() = Header.builder()
    .key(key)
    .value(value.toByteArray())
    .build()!!

fun Message.toNode(queueId: String): Node {
    return Node(
        id,
        schemaId,
        payload.toByteArray(),
        headersList.map { it.toHeader() },
        replayId.toByteArray(),
        queueId
    )
}