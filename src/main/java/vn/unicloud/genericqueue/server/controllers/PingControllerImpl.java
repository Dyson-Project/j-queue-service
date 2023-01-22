package vn.unicloud.genericqueue.server.controllers;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.unicloud.eventbus.protobuf.PingRequest;
import vn.unicloud.eventbus.protobuf.PingResponse;
import vn.unicloud.eventbus.protobuf.PingServiceGrpc;

@GrpcService
public class PingControllerImpl extends PingServiceGrpc.PingServiceImplBase {
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder()
                .setPong(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void pingStream(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        while(true){
            responseObserver.onNext(PingResponse.newBuilder()
                    .setPong(true)
                    .build());
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
