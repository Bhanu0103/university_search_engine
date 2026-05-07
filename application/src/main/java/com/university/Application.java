package com.university;

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
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("==================================================");
        System.out.println("University Search Backend System Started Successfully!");
        System.out.println("Modular Monolith Workflow: All 13 modules are loaded.");
        System.out.println("gRPC Server is listening for requests...");
        System.out.println("==================================================");
    }
}
