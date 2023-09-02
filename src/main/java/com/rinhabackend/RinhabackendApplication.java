package com.rinhabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class RinhabackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RinhabackendApplication.class, args);
    }

}

