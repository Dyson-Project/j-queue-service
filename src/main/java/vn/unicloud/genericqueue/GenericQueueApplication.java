package vn.unicloud.genericqueue;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.unicloud.genericqueue.server.services.GenericQueueServiceImpl;

import java.io.IOException;

@SpringBootApplication
public class GenericQueueApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(5050)
                .addService(new GenericQueueServiceImpl())
                .build();
        server.start();
        server.awaitTermination();
    }

}
