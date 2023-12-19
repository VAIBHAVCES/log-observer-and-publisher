package com.log.monitoring.service;

import com.log.monitoring.service.configurations.FileWatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ServiceApplication {



	public static void main(String[] args) {

		SpringApplication.run(ServiceApplication.class, args);



	}

}
