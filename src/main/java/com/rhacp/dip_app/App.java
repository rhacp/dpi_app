package com.rhacp.dip_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {

    public static void main( String[] args ) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(App.class, args);
    }
}
