package com.tunahan.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class TunahanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TunahanApplication.class, args);
	}

}
