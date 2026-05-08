package com.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackages = "com.university",
    exclude = {net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration.class}
)
@EnableJpaRepositories(basePackages = "com.university")
@EntityScan(basePackages = "com.university")
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("University Search Backend System started successfully");
        logger.info("Modular monolith workflow: all 13 modules are loaded");
        logger.info("gRPC server is listening for requests");
    }
}
