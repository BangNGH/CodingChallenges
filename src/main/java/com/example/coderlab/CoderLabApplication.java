package com.example.coderlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CoderLabApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoderLabApplication.class, args);
	}
}
