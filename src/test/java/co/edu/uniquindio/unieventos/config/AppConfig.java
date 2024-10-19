package co.edu.uniquindio.unieventos.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.config.TokenFilter;

@Configuration
public class AppConfig {

	@Bean
	@PostConstruct
	@ConditionalOnMissingBean
	public AuthUtils authUtils() {
		System.out.println("AppConfig.authUtils()");
		return new AuthUtils();
	}

	@Bean
	@ConditionalOnMissingBean
	@PostConstruct
	public JWTUtils jwtUtils() {
		return new JWTUtils();
	}

	@Bean
	@PostConstruct
	@ConditionalOnMissingBean
	public TokenFilter tokenFilter() {
		return new TokenFilter();
	}
}
