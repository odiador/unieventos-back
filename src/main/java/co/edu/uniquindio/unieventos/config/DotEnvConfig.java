package co.edu.uniquindio.unieventos.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotEnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		try (InputStream is = getClass().getResourceAsStream("/.env")) {
			if (is == null) {
				System.err.println("Ignoring dotEnv");
				return;
			}
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			Dotenv dotenv = Dotenv.load();
			Map<String, Object> envMap = new HashMap<>();
			dotenv.entries().forEach(entry -> {
				envMap.put(entry.getKey(), entry.getValue());
				System.setProperty(entry.getKey(), entry.getValue());
			});
			environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envMap));
			System.out.println("Loaded env variables");

		} catch (Exception e) {
			System.err.println("Ignoring dotEnv");
		}
	}
}
