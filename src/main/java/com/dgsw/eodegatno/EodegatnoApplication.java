package com.dgsw.eodegatno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EodegatnoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EodegatnoApplication.class, args);
    }

}
