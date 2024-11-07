package co.edu.uniquindio.unieventos.config;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotEnvConfig {

	@PostConstruct
	public void loadEnv() {
		try (InputStream is = getClass().getResourceAsStream("/.env")) {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
			System.out.println("Loaded env variables");

		} catch (Exception e) {
			System.err.println("Ignoring dotEnv");
		}
	}
}
