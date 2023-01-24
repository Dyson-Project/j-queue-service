package vn.unicloud.genericqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GenericQueueApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(GenericQueueApplication.class, args);
    }
}
