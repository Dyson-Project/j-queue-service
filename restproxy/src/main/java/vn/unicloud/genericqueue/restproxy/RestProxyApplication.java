package vn.unicloud.genericqueue.restproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"vn.unicloud.genericequeue"})
public class RestProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestProxyApplication.class, args);
    }

}
