package vn.unicloud.genericqueue.server.controllers;

import com.google.protobuf.ByteString;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.reflection.v1alpha.ErrorResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.protobuf.Error;
import vn.unicloud.genericqueue.server.services.QueueManager;

import javax.inject.Inject;

@GrpcService
public class GenericQueueServiceControllerImpl extends GenericQueueServiceGrpc.GenericQueueServiceImplBase {
    @Inject
    private QueueManager queueManager;

    @Override
    public void subscribe(FetchRequest request, StreamObserver<FetchResponse> responseObserver) {
        while (true) {
            responseObserver.onNext(null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StreamObserver<FetchRequest> subscribeStream(StreamObserver<FetchResponse> responseObserver) {
        return new StreamObserver<FetchRequest>() {
            @Override
            public void onNext(FetchRequest value) {
                byte[] event = (byte[]) queueManager.getQueue(value.getTopicName()).poll();
                responseObserver.onNext(FetchResponse.newBuilder()
                        .addMessages(ConsumerMessage.newBuilder()
                                .setPayload(ByteString.copyFrom(event))
                                .build())
                        .build());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void publish(PublishRequest request, StreamObserver<PublishResponse> responseObserver) {
        String topic = request.getTopicName();

        request.getMessagesList().forEach(e -> {
            queueManager.offer(
                    topic,
                    request.getQueueIndex(),
                    e.getPayload().toByteArray()
            );
        });

        PublishResponse res = PublishResponse.newBuilder()
                .addResults(PublishResult.newBuilder().build())
                .build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PublishRequest> publishStream(StreamObserver<PublishResponse> responseObserver) {
        return new StreamObserver<PublishRequest>() {
            @Override
            public void onNext(PublishRequest value) {
                String topic = value.getTopicName();

                value.getMessagesList().forEach(e -> {
                    queueManager.offer(
                            topic,
                            value.getQueueIndex(),
                            e.getPayload().toByteArray()
                    );
                });

                PublishResponse res = PublishResponse.newBuilder()
                        .addResults(PublishResult.newBuilder().build())
                        .build();
                responseObserver.onNext(res);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getList(GetListRequest request, StreamObserver<FetchResponse> responseObserver) {
        responseObserver.onNext(queueManager.getList(request));
        responseObserver.onCompleted();
    }
}
