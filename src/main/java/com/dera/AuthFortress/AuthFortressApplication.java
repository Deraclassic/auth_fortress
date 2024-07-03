package com.dera.AuthFortress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.dera.AuthFortress")
public class AuthFortressApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthFortressApplication.class, args);
	}

}
