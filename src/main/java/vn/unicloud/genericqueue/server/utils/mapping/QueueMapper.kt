package vn.unicloud.genericqueue.server.utils.mapping

import vn.unicloud.genericequeue.persistence.entities.Queue
import vn.unicloud.genericqueue.protobuf.Message
import vn.unicloud.genericqueue.server.algorithm.GenericQueue

fun GenericQueue<Message>.toQueue(topic: String) = Queue.builder()
    .id(id)
    .topic(topic)
    .build()!!