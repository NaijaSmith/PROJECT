package com.resourcify.resourcify_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.config",
    "com.resourcify.resourcifybackend.controller",
    "com.resourcify.resourcifybackend.model",
    "com.resourcify.resourcifybackend.repository"
})
public class ResourcifyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourcifyBackendApplication.class, args);
	}

}
