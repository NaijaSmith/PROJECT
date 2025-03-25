package com.resourcify.resourcify_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
    "com.config",
    "com.resourcify.resourcify_backend.controller",
    "com.resourcify.resourcify_backend.model",
    "com.resourcify.resourcify_backend.repository",
    "com.resourcify.resourcify_backend.util"
})
public class ResourcifyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourcifyBackendApplication.class, args);
	}

}
