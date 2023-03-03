package com.uliga.uliga_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UligaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UligaBackendApplication.class, args);
    }

}
