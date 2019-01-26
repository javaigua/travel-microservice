package com.klm.casex01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
public class Casex01Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Casex01Application.class, args);
        context.registerShutdownHook();
    }
}
