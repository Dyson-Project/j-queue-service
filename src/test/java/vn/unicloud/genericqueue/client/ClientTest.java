package vn.unicloud.genericqueue.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vn.unicloud.eventbus.protobuf.*;
import vn.unicloud.schema.protobuf.GAgent;

import javax.annotation.Nullable;

public class ClientTest {

//    ManagedChannel channel;
//    GenericQueueServiceGrpc.GenericQueueServiceStub stub;
//
//    @BeforeAll
//    public void beforeAll() {
//        channel = ManagedChannelBuilder
//                .forAddress("127.0.0.1", 5050)
//                .usePlaintext()
//                .build();
//        stub = GenericQueueServiceGrpc.newStub(channel);
//    }
//
//    @Test
//    public void testPublish() {
//        PublishRequest request = PublishRequest.newBuilder()
//                .setTopicName("hihi")
//                .addEvents(ProducerEvent.newBuilder()
//                        .setPayload(GAgent.newBuilder()
//                                .setId("100")
//                                .setCif("cif")
//                                .setName("simple name")
//                                .build().toByteString())
//                        .setId("1")
//                        .setSchemaId("1")
//                        .build())
//                .build();
//        stub.publish(request, new ClientCallStreamObserver<PublishResponse>() {
//            @Override
//            public void cancel(@Nullable String message, @Nullable Throwable cause) {
//
//            }
//
//            @Override
//            public boolean isReady() {
//                return true;
//            }
//
//            @Override
//            public void setOnReadyHandler(Runnable onReadyHandler) {
//
//            }
//
//            @Override
//            public void request(int count) {
//
//            }
//
//            @Override
//            public void setMessageCompression(boolean enable) {
//
//            }
//
//            @Override
//            public void disableAutoInboundFlowControl() {
//
//            }
//
//            @Override
//            public void onNext(PublishResponse value) {
//                System.out.println("server response");
//                System.out.println(value);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        });
//        int count = 0;
//        while (count < 10) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            count++;
//            System.out.println("wait for fetch...");
//        }
//    }

    @Test
    public void testSubscribe() {
//        stub.subscribe(new PubSubEventObserver());
    }
}
