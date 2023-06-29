package com.uliga.uliga_backend.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource")
public class DatabaseProperty {
    private String url;
    private List<Slave> slaveList;
    private String username;
    private String password;
    private String driverClassName;

    @Getter
    @Setter
    public static class Slave {
        private String name;
        private String url;
    }
}
