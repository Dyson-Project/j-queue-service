package vn.unicloud.genericqueue.server.services;

import com.google.rpc.Code;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import vn.unicloud.genericqueue.exceptions.InternalException;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.server.algorithm.CircularQueue;
import vn.unicloud.genericqueue.server.utils.mapping.MessageMapperKt;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Log4j2
public class QueueManager {
    private Map<String, Queue<Message>[]> topicMap = new ConcurrentHashMap<>();

    public Queue getQueue(String topic) {
        return null;
    }

    public boolean offer(String topic, int queueIndex, Message message) {
        return topicMap.get(topic)[queueIndex].offer(message);
    }

    public void publish(PublishRequest request) {
        String topic = request.getTopicName();
        request.getMessagesList().forEach(e -> {
            boolean offerResult = offer(
                    topic,
                    request.getQueueIndex(),
                    MessageMapperKt.toMessage(e)
            );
            log.info("offer rs: {}, {}, size {}", e.getId(), offerResult, topicMap.get(topic)[request.getQueueIndex()].size());
        });
        log.info(topicMap.get(topic)[0]);
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
        Queue<Message> queue = topicMap.get(topicName)[queueIndex];
        Iterable<Message> messages = queue.stream()
                .skip(request.getOffset())
                .limit(request.getLimit())::iterator;
        return FetchResponse.newBuilder()
                .addAllMessages(messages)
                .build();
    }

    private boolean isQueueExisted(String topicName, int queueIndex) {
        if (topicMap.get(topicName) == null || topicMap.get(topicName)[queueIndex] == null) {
            return false;
        }
        return true;
    }

    public void createQueueGroup(String topicName, QueueType type, int topicSize){
        Queue<Message>[] queues = Stream.generate(() -> this.newQueue(type))
                .limit(topicSize).toArray(Queue[]::new);
        topicMap.put(topicName, queues);
    }
    public void deleteQueueGroup(String topicName){
        topicMap.remove(topicName);
    }

    private Queue<byte[]> newQueue(QueueType type) {
        switch (type) {
            case CIRCULAR_LINKED_LIST:
            case CIRCULAR_ARRAY:
                return new CircularQueue<>();
            default:
                throw new RuntimeException("invalid queue type");
        }
    }
}
