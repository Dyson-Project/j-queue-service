package vn.unicloud.genericqueue.server.controllers;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.server.services.QueueManager;

import javax.inject.Inject;

@GrpcService
public class GenericQueueControllerImpl extends GenericQueueServiceGrpc.GenericQueueServiceImplBase {
    @Inject
    private QueueManager queueManager;

    @Override
    public void subscribe(FetchRequest request, StreamObserver<FetchResponse> responseObserver) {
        Message m;
        while (true) {
            while ((m = queueManager.fetch(request)) == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            responseObserver.onNext(FetchResponse.newBuilder()
                    .addMessages(m)
                    .build());
        }
    }

    @Override
    public StreamObserver<FetchRequest> subscribeStream(StreamObserver<FetchResponse> responseObserver) {
        return super.subscribeStream(responseObserver);
//        return new StreamObserver<FetchRequest>() {
//            @Override
//            public void onNext(FetchRequest value) {
//                byte[] event = (byte[]) queueManager.getQueue(value.getTopicName()).poll();
//                responseObserver.onNext(FetchResponse.newBuilder()
//                        .addMessages(Message.newBuilder()
//                                .setPayload(ByteString.copyFrom(event))
//                                .build())
//                        .build());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
//            }
//
//            @Override
//            public void onCompleted() {
//                responseObserver.onCompleted();
//            }
//        };
    }

    @Override
    public void publish(PublishRequest request, StreamObserver<PublishResponse> responseObserver) {
        responseObserver.onNext(queueManager.publish(request));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PublishRequest> publishStream(StreamObserver<PublishResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(PublishRequest value) {
                responseObserver.onNext(queueManager.publish(value));
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
