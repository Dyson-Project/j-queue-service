package vn.unicloud.genericqueue.server.services;

import io.grpc.stub.StreamObserver;
import vn.unicloud.eventbus.protobuf.*;

public class GenericQueueServiceImpl extends GenericQueueServiceGrpc.GenericQueueServiceImplBase {

    @Override
    public StreamObserver<FetchRequest> subscribe(StreamObserver<FetchResponse> responseObserver) {
        return new StreamObserver<FetchRequest>() {
            @Override
            public void onNext(FetchRequest value) {
                responseObserver.onNext();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getSchema(SchemaRequest request, StreamObserver<SchemaInfo> responseObserver) {
        super.getSchema(request, responseObserver);
    }

    @Override
    public void getTopic(TopicRequest request, StreamObserver<TopicInfo> responseObserver) {
        super.getTopic(request, responseObserver);
    }

    @Override
    public void publish(PublishRequest request, StreamObserver<PublishResponse> responseObserver) {
        super.publish(request, responseObserver);
    }

    @Override
    public StreamObserver<PublishRequest> publishStream(StreamObserver<PublishResponse> responseObserver) {
        return super.publishStream(responseObserver);
    }
}
