package org.mehmetkatr.meridian.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "org.mehmetkatr.meridian")
@EnableJpaAuditing
public class LedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
	}

}
