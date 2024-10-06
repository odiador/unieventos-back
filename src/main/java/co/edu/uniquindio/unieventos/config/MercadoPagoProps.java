package co.edu.uniquindio.unieventos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "custom.payments")
@Getter
@Setter
public class MercadoPagoProps {

	private String accesstoken, ngrokurl;
}
