package vn.unicloud.genericqueue.server.services;

import org.springframework.stereotype.Service;
import vn.unicloud.genericequeue.persistence.entities.Node;
import vn.unicloud.genericequeue.persistence.entities.Queue;
import vn.unicloud.genericequeue.persistence.entities.Topic;
import vn.unicloud.genericequeue.persistence.repositories.NodeRepository;
import vn.unicloud.genericequeue.persistence.repositories.QueueRepository;
import vn.unicloud.genericequeue.persistence.repositories.TopicRepository;
import vn.unicloud.genericqueue.protobuf.Message;
import vn.unicloud.genericqueue.protobuf.TopicInfo;
import vn.unicloud.genericqueue.protobuf.TopicProto;
import vn.unicloud.genericqueue.server.algorithm.GenericQueue;
import vn.unicloud.genericqueue.server.utils.mapping.MessageMapperKt;
import vn.unicloud.genericqueue.server.utils.mapping.QueueMapperKt;
import vn.unicloud.genericqueue.server.utils.mapping.TopicMapperKt;

import javax.inject.Inject;

@Service
public class QueuePersistenceProxy {
    @Inject
    private NodeRepository nodeRepository;

    @Inject
    private QueueRepository queueRepository;

    @Inject
    private TopicRepository topicRepository;

    public Node createNode(Message message, String queueId) {
        return nodeRepository.save(MessageMapperKt.toNode(message, queueId));
    }

    public void removeNode(String nodeId) {
        nodeRepository.deleteById(nodeId);
    }

    public Queue createQueue(GenericQueue<Message> queue, String topicName) {
        return queueRepository.save(QueueMapperKt.toQueue(queue, topicName));
    }

    public Topic createTopic(TopicInfo topic) {
        return topicRepository.save(TopicMapperKt.toTopic(topic));
    }

    public void removeTopic(String topicName) {
        topicRepository.deleteById(topicName);
    }


}
