package co.edu.uniquindio.unieventos.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotEnvConfig {

	@PostConstruct
	public void loadEnv() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(
				entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}
}
