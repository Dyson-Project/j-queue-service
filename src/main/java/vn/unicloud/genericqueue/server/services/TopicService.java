package vn.unicloud.genericqueue.server.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.unicloud.genericqueue.protobuf.*;

import java.util.UUID;

@Service
@Slf4j
public class TopicService {
    @Autowired
    private QueueManager queueManager;

    public TopicInfo createTopic(CreateTopicRequest request) {
        String topicName = request.getTopicName();
        QueueType queueType = request.getQueueType();
        int topicSize = request.getTopicSize();
        queueManager.createQueueGroup(topicName, queueType, topicSize);
        return TopicInfo.newBuilder()
                .setTopicName(topicName)
                .setSchemaId(UUID.randomUUID().toString())
                .setQueueType(queueType)
                .setTopicSize(topicSize)
                .build();
    }

    public DeleteTopicResponse deleteTopic(DeleteTopicRequest request){
        String topicName= request.getTopicName();
        queueManager.deleteQueueGroup(topicName);
        return DeleteTopicResponse.newBuilder()
                .build();
    }
}
