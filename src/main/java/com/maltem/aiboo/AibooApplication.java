package com.maltem.aiboo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(scanBasePackages="com.maltem.aiboo.config")//(exclude =  {DataSourceAutoConfiguration.class })
public class AibooApplication {

	public static void main(String[] args) {
		SpringApplication.run(AibooApplication.class, args);
	}

}
