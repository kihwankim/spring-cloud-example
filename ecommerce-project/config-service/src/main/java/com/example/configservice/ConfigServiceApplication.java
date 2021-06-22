package com.example.configservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {
    @Autowired
    A a;

    @PostConstruct
    void hello() {
        a.print();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }

    @Component
    private static class A {
        @Autowired
        Environment environment;

        public void print() {
            System.out.println(environment.getProperty("spring.cloud.config.server.git.uri"));
        }
    }

}
