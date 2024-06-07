package com.ftn.sbnz.service;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = {"com.ftn.sbnz.model.models"})
public class ServiceApplication  {
    
    private static Logger log = LoggerFactory.getLogger(ServiceApplication.class);
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ServiceApplication.class, args);

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        StringBuilder sb = new StringBuilder("Application beans:\n");
        for (String beanName : beanNames) {
            sb.append(beanName + "\n");
        }
        log.info(sb.toString());
    }
}
