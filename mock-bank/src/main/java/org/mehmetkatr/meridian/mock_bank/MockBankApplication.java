package org.mehmetkatr.meridian.mock_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "org.mehmetkatr.meridian")
@EnableJpaAuditing
public class MockBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockBankApplication.class, args);
	}

}
