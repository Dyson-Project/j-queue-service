package vn.unicloud.genericqueue.server;

import io.grpc.stub.ServerCallStreamObserver;

public abstract class ServerCallStreamObserverBaseImpl<RespT> extends ServerCallStreamObserver<RespT> {
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setOnCancelHandler(Runnable onCancelHandler) {

    }

    @Override
    public void setCompression(String compression) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setOnReadyHandler(Runnable onReadyHandler) {

    }

    @Override
    public void disableAutoInboundFlowControl() {

    }

    @Override
    public void request(int count) {

    }

    @Override
    public void setMessageCompression(boolean enable) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}
