package vn.unicloud.genericqueue.server.services;

import com.google.protobuf.ByteString;
import com.google.rpc.Code;
import io.grpc.Status;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.stereotype.Service;
import vn.unicloud.genericqueue.exceptions.InternalException;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.server.algorithm.CircularQueue;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Log4j2
public class QueueManager {
    private Map<String, Queue<byte[]>[]> topicMap = new ConcurrentHashMap<>();

    public Queue getQueue(String topic) {
        return null;
    }

    public void offer(String topic, int queueIndex, byte[] payload) {
        topicMap.get(topic)[queueIndex].offer(payload);
    }

    public void createTopic(CreateTopicRequest request) {
        String topicName = request.getTopicName();
        QueueType queueType = request.getQueueType();
        log.info("create topic {}", topicName);
        Queue<byte[]>[] queues = (Queue<byte[]>[]) Stream.generate(() -> this.newQueue(queueType))
                .limit(request.getTopicSize()).toArray();
        topicMap.put(topicName, queues);
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
        Queue<byte[]> queue = topicMap.get(request.getTopicName())[request.getQueueIndex()];
        Iterable<ConsumerMessage> messages= (Iterable<ConsumerMessage>) queue.stream()
                .skip(request.getOffset())
                .limit(request.getLimit())
                .map(e -> ConsumerMessage.newBuilder()
                        .setPayload(ByteString.copyFrom(e))
                        .build()).iterator();
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
