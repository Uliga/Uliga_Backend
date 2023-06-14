package com.uliga.uliga_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UligaBackendApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:profile-application.yml";


    public static void main(String[] args) {
//        SpringApplication.run(UligaBackendApplication.class, args);
        new SpringApplicationBuilder(UligaBackendApplication.class).properties(APPLICATION_LOCATIONS).run(args);
    }

}
