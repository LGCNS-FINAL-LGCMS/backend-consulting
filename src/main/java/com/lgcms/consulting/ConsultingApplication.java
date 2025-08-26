package com.lgcms.consulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
public class ConsultingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultingApplication.class, args);
    }

}
