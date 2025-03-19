package me.wjz.creeperhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CreeperHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreeperHubApplication.class, args);
    }

}
