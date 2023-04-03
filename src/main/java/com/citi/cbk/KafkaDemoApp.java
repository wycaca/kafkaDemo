package com.citi.cbk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaDemoApp {
    private final Logger logger = LoggerFactory.getLogger(KafkaDemoApp.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApp.class, args);
    }

}
