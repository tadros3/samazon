package samazon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@SpringBootApplication
@EnableEmailTools
public class SamazonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamazonApplication.class, args);
	}
}
