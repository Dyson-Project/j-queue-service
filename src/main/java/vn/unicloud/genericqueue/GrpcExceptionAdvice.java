package vn.unicloud.genericqueue;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import vn.unicloud.genericqueue.exceptions.InternalException;

@GrpcAdvice
public class GrpcExceptionAdvice {
    @GrpcExceptionHandler(InternalException.class)
    public Status handleInternalException(InternalException e){
        return Status
                .fromCodeValue(e.getCode())
                .withDescription(e.getMessage())
                .withCause(e);
    }
}
