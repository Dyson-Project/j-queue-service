package vn.unicloud.genericqueue.server.controllers;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.unicloud.genericqueue.protobuf.*;
import vn.unicloud.genericqueue.server.services.QueueManager;

import javax.inject.Inject;

@GrpcService
@Slf4j
public class TopicControllerImpl extends TopicServiceGrpc.TopicServiceImplBase {
    @Inject
    private QueueManager queueManager;

    @Override
    public void getTopic(GetTopicRequest request, StreamObserver<TopicInfo> responseObserver) {
        super.getTopic(request, responseObserver);
    }

    @Override
    public void createTopic(CreateTopicRequest request, StreamObserver<TopicInfo> responseObserver) {
        responseObserver.onNext(
                queueManager.createTopic(request)
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTopic(DeleteTopicRequest request, StreamObserver<DeleteTopicResponse> responseObserver) {
        super.deleteTopic(request, responseObserver);
    }
}
