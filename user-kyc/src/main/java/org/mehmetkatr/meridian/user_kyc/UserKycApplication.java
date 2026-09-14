package org.mehmetkatr.meridian.user_kyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserKycApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserKycApplication.class, args);
	}

}
