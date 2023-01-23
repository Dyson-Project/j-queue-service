package vn.unicloud.genericqueue.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vn.unicloud.genericqueue.protobuf.CreateTopicRequest;
import vn.unicloud.genericqueue.protobuf.SchemaInfo;
import vn.unicloud.genericqueue.protobuf.TopicServiceGrpc;

@SpringBootTest
public class TopicTest {
    static final String TOPIC = "test";
    @GrpcClient("qc")
    private TopicServiceGrpc.TopicServiceBlockingStub stub;

    @Test
    public void create(){
        stub.createTopic(CreateTopicRequest.newBuilder()
                        .setTopicName(TOPIC)
                        .setTopicSize(1)
                        .setSchema(SchemaInfo.newBuilder()
                                .build())
                .build());
    }

}
