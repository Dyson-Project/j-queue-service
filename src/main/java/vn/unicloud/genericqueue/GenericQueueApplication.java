package vn.unicloud.genericqueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.unicloud.genericqueue.server.controllers.GenericQueueServiceControllerImpl;
import vn.unicloud.genericqueue.server.controllers.PingControllerImpl;

import javax.inject.Inject;
import java.io.IOException;

@SpringBootApplication
public class GenericQueueApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(GenericQueueApplication.class, args);
    }
}
