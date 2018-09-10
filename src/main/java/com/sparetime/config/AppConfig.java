package com.sparetime.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sparetime"})
public class AppConfig {

	public static void main(String[] args) {
		new SpringApplication(AppConfig.class, DBConfig.class).run(args);
	}
}
