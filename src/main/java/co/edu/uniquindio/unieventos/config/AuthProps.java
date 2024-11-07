package co.edu.uniquindio.unieventos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "custom.auth")
@Getter
@Setter
public class AuthProps {

	private String clientsecret;
}
