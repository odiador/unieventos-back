package co.edu.uniquindio.unieventos.test;

import org.junit.jupiter.api.Test;

import co.edu.uniquindio.unieventos.config.DotEnvConfig;
import co.edu.uniquindio.unieventos.config.MercadoPagoProps;

public class OrderTest {

	@Test
	public void test() throws Exception {
		try {
			new DotEnvConfig().loadEnv();
			MercadoPagoProps properties = new MercadoPagoProps();
			properties.setAccesstoken(System.getProperty("custom.payments.accesstoken"));
			properties.setNgrokurl(System.getProperty("custom.payments.ngrokurl"));
//			OrderServiceImpl service = new OrderServiceImpl(properties);
//			service.realizarPago("1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
