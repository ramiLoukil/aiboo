package com.maltem.aiboo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;

@SpringBootApplication(exclude = {QuartzAutoConfiguration.class })
public class AibooApplication {

	public static void main(String[] args) {
		SpringApplication.run(AibooApplication.class, args);
	}

}
