package vn.unicloud.genericqueue.server.utils.mapping

import vn.unicloud.genericequeue.persistence.entities.Topic
import vn.unicloud.genericqueue.protobuf.TopicInfo

fun TopicInfo.toTopic() = Topic.builder()
    .name(topicName)
    .topicSize(topicSize)
    .queueType(queueTypeValue)
    .schemaId(schemaId)
    .tenantGuid(tenantGuid)
    .build()!!
