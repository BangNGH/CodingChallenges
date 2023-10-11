package com.example.coderlab;

import com.example.coderlab.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class CoderLabApplication {


	public static void main(String[] args) {
		SpringApplication.run(CoderLabApplication.class, args);
	}

}
