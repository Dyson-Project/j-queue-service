package vn.unicloud.genericqueue.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import vn.unicloud.eventbus.protobuf.PingResponse;
import vn.unicloud.genericqueue.protobuf.*;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class GenericQueueTest {
    Logger log = LoggerFactory.getLogger(GenericQueueTest.class);
    static final String TOPIC = "test";
    static final String GRPC_CLIENT = "qc";

    @GrpcClient(GRPC_CLIENT)
    private GenericQueueServiceGrpc.GenericQueueServiceBlockingStub stub;
    @GrpcClient(GRPC_CLIENT)
    private TopicServiceGrpc.TopicServiceBlockingStub topicStub;

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
    public void testPubGetList() {
        int totalMessageCount = 10;
        int fetchMessageLimit = 5;
        int fetchMessageOffset = 6;
        int queueIndex = 0;
        List<ProducerMessage> messages = Stream.generate(() ->
                        ProducerMessage.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setPayload(PingResponse
                                        .newBuilder()
                                        .setPong(true)
                                        .build().toByteString())
                                .build())
                .limit(totalMessageCount)
                .collect(Collectors.toList());
        stub.publish(PublishRequest.newBuilder()
                .setTopicName(TOPIC)
                .setQueueIndex(queueIndex)
                .addAllMessages(messages)
                .build());

        Iterator itr = stub.getList(GetListRequest.newBuilder()
                .setTopicName(TOPIC)
                .setQueueIndex(queueIndex)
                .setOffset(fetchMessageOffset)
                .setLimit(fetchMessageLimit)
                .build());
        log.info("Test result:");
        while (itr.hasNext()) {
            log.info("-> {}", itr.next());
        }
    }

    @AfterEach
    public void afterEach() {
        topicStub.deleteTopic(DeleteTopicRequest.newBuilder().setTopicName(TOPIC).build());
    }

}
