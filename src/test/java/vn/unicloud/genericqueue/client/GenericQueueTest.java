package vn.unicloud.genericqueue.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import vn.unicloud.eventbus.protobuf.PingResponse;
import vn.unicloud.genericqueue.protobuf.*;

import javax.annotation.security.RunAs;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenericQueueTest {
    Logger log = LoggerFactory.getLogger(GenericQueueTest.class);
    static final String TOPIC = "test";
    static final String GRPC_CLIENT = "qc";
    static final long TEST_TIMEOUT = 5_000;

    @GrpcClient(GRPC_CLIENT)
    private GenericQueueServiceGrpc.GenericQueueServiceBlockingStub stub;
    @GrpcClient(GRPC_CLIENT)
    private TopicServiceGrpc.TopicServiceBlockingStub topicStub;
    List<ProducerMessage> messages;

    @BeforeAll
    public void beforeAll() {
        int totalMessageCount = 10;
        messages = Stream.generate(() ->
                        ProducerMessage.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setPayload(PingResponse
                                        .newBuilder()
                                        .setPong(true)
                                        .build().toByteString())
                                .build())
                .limit(totalMessageCount)
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void beforeEach() {
        topicStub.createTopic(CreateTopicRequest.newBuilder()
                .setTopicName(TOPIC)
                .setTopicSize(1)
                .setSchema(SchemaInfo.newBuilder()
                        .build())
                .build());
    }

    @Test
    @Timeout(5)
    public void testPubGetList() {
        int fetchMessageLimit = 5;
        int fetchMessageOffset = 6;
        int queueIndex = 0;
        stub.publish(PublishRequest.newBuilder()
                .setTopicName(TOPIC)
                .setQueueIndex(queueIndex)
                .addAllMessages(messages)
                .build());

        new FetchStreamWorker("testPubGetList",
                stub.getList(GetListRequest.newBuilder()
                        .setTopicName(TOPIC)
                        .setQueueIndex(queueIndex)
                        .setOffset(fetchMessageOffset)
                        .setLimit(fetchMessageLimit)
                        .build()),
                TEST_TIMEOUT
        ).run();
    }

    @Test
    @Timeout(5)
    public void testPubSub() {
        int queueIndex = 0;
        stub.publish(PublishRequest.newBuilder()
                .setTopicName(TOPIC)
                .setQueueIndex(queueIndex)
                .addAllMessages(messages)
                .build());

        new FetchStreamWorker("testPubSub",
                stub.subscribe(FetchRequest.newBuilder()
                        .setTopicName(TOPIC)
                        .setQueueIndex(queueIndex)
                        .setReplayPreset(ReplayPreset.EARLIEST)
                        .build()),
                TEST_TIMEOUT
        ).run();
    }

    @Test
    @Timeout(5)
    public void test2Subscriber() {
        int queueIndex = 0;
        stub.publish(PublishRequest.newBuilder()
                .setTopicName(TOPIC)
                .setQueueIndex(queueIndex)
                .addAllMessages(messages)
                .build());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            executor.execute(new FetchStreamWorker(String.valueOf(i),
                    stub.subscribe(FetchRequest.newBuilder()
                            .setTopicName(TOPIC)
                            .setQueueIndex(queueIndex)
                            .setReplayPreset(ReplayPreset.EARLIEST)
                            .build()),
                    TEST_TIMEOUT
            ));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        log.info("Finished all threads");
    }

    @AfterEach
    public void afterEach() {
        topicStub.deleteTopic(DeleteTopicRequest.newBuilder().setTopicName(TOPIC).build());
    }

    static class FetchStreamWorker implements Runnable {
        Logger log = LoggerFactory.getLogger(GenericQueueTest.class);
        Iterator subscriber;
        String workerId;
        long timeout;

        public FetchStreamWorker(String workerId, Iterator itr, long timeout) {
            this.workerId = workerId;
            subscriber = itr;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            long end = System.currentTimeMillis() + timeout;
            while (subscriber.hasNext()) {
                log.info("subscriber {} received: {}", workerId, subscriber.next());
                if(System.currentTimeMillis() >= end){
                    break;
                }
            }
        }
    }
}
