package pl.mjbladaj.zaaw_java.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("pl.mjbladaj.zaaw_java.server.dao")
public class ZaawJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaawJavaApplication.class, args);
	}
}
