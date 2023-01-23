package vn.unicloud.genericqueue.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import vn.unicloud.eventbus.protobuf.PingRequest;
import vn.unicloud.eventbus.protobuf.PingResponse;
import vn.unicloud.eventbus.protobuf.PingServiceGrpc;
import vn.unicloud.genericqueue.GenericQueueApplication;
import vn.unicloud.genericqueue.server.services.QueueManager;

import java.util.Iterator;

@SpringBootTest
public class PingTest {
    @GrpcClient("qc")
    private PingServiceGrpc.PingServiceBlockingStub pingStub;


    @Test
    public void ping() {
        PingResponse res= pingStub.ping(PingRequest.newBuilder().build());
        System.out.println(res);
    }

    @Test
    public void pingStream(){
        Iterator<PingResponse> pings = pingStub.pingStream(PingRequest.newBuilder().build());
        while(pings.hasNext()){
            System.out.printf(pings.next().toString());
        }
    }
}
