package vn.unicloud.genericqueue.server.services;

import com.google.rpc.Code;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import vn.unicloud.genericequeue.persistence.entities.Queue;
import vn.unicloud.genericequeue.persistence.repositories.NodeRepository;
import vn.unicloud.genericequeue.persistence.repositories.QueueRepository;
import vn.unicloud.genericequeue.persistence.repositories.TopicRepository;
import vn.unicloud.genericqueue.exceptions.InternalException;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.server.algorithm.CircularQueue;
import vn.unicloud.genericqueue.server.algorithm.GenericQueue;
import vn.unicloud.genericqueue.server.utils.mapping.MessageMapperKt;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Log4j2
public class QueueManager implements InitializingBean {
    private final Map<String, GenericQueue<Message>[]> topicMap = new ConcurrentHashMap<>();

    @Inject
    private NodeRepository nodeRepository;

    @Inject
    private QueueRepository queueRepository;

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private QueuePersistenceProxy queuePersistenceProxy;

    @Override
    public void afterPropertiesSet() {
        restartQueues();
    }

    public void restartQueues() {
        topicMap.clear();
        topicRepository.findAll().forEach(topic -> {
            String topicName = topic.getName();
            Iterable<Queue> queuesItr = queueRepository.findAllByTopic(topicName);
            GenericQueue<Message>[] queues = StreamSupport.stream(queuesItr.spliterator(), true).map(queue ->
                    newQueue(topicName, QueueType.forNumber(topic.getQueueType()), queue.getId())
            ).toArray(GenericQueue[]::new);
            topicMap.put(topic.getName(), queues);
        });
        System.out.println(topicMap);
    }

    public PublishResponse publish(PublishRequest request) {
        String topic = request.getTopicName();
        PublishResponse.Builder responseBuilder = PublishResponse.newBuilder();
        request.getMessagesList().forEach(e -> {
            Message message = MessageMapperKt.toMessage(e);
            boolean offerResult = offer(
                    topic,
                    request.getQueueIndex(),
                    message
            );
            responseBuilder.addResults(
                    PublishResult.newBuilder()
                            .setReplayId(message.getReplayId())
                            .build()
            );
            log.debug("offer rs: {}, {}, size {}", e.getId(), offerResult, topicMap.get(topic)[request.getQueueIndex()].size());
        });
        return responseBuilder.build();
    }

    @SneakyThrows
    public Message fetch(FetchRequest request) {
        log.debug("call fetch");
        String topic = request.getTopicName();
        int queueIndex = request.getQueueIndex();
        if (!isQueueExisted(topic, queueIndex)) {
            throw InternalException.builder()
                    .message("Queue not found")
                    .code(Code.NOT_FOUND_VALUE)
                    .build();
        }
        // TODO: write replay
//        ReplayPreset replayPreset = request.getReplayPreset();
//        ByteString replayId = request.getReplayId();
        return topicMap.get(topic)[queueIndex].poll();
    }

    @SneakyThrows
    public FetchResponse getList(GetListRequest request) {
        String topicName = request.getTopicName();
        int queueIndex = request.getQueueIndex();
        if (!isQueueExisted(topicName, queueIndex)) {
            throw InternalException.builder()
                    .code(Code.NOT_FOUND_VALUE)
                    .message("Queue not found")
                    .build();
        }
        GenericQueue<Message> queue = topicMap.get(topicName)[queueIndex];
        Iterable<Message> messages = queue.stream()
                .skip(request.getOffset())
                .limit(request.getLimit())::iterator;
        return FetchResponse.newBuilder()
                .addAllMessages(messages)
                .build();
    }

    public void createQueueGroup(String topicName, QueueType type, int topicSize) {
        GenericQueue<Message>[] queues = Stream.generate(() -> this.newQueue(topicName, type))
                .limit(topicSize).toArray(GenericQueue[]::new);
        topicMap.put(topicName, queues);
    }

    public void deleteQueueGroup(String topicName) {
        topicMap.remove(topicName);
    }

    private boolean isQueueExisted(String topicName, int queueIndex) {
        return topicMap.get(topicName) != null && topicMap.get(topicName)[queueIndex] != null;
    }

    private boolean offer(String topic, int queueIndex, Message message) {
        GenericQueue<Message> queue = topicMap.get(topic)[queueIndex];
        queuePersistenceProxy.createNode(message, queue.getId());
        return queue.offer(message);
    }

    private GenericQueue<Message> newQueue(String topicName, QueueType type) {
        return newQueue(topicName, type, UUID.randomUUID().toString());
    }

    private GenericQueue<Message> newQueue(String topicName, QueueType type, String queueId) {
        switch (type) {
            case CIRCULAR_LINKED_LIST:
            case CIRCULAR_ARRAY: {
                CircularQueue<Message> q = new CircularQueue<>(queueId);
                queuePersistenceProxy.createQueue(q, topicName);
                return q;
            }
            default:
                throw new RuntimeException("invalid queue type");
        }
    }

}
