package com.shop.order;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		LoggerFactory.getLogger(OrderServiceApplication.class)
				.info("JVM timezone forcibly set to UTC");
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}