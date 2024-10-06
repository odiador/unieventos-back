package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.config.MercadoPagoProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnieventosApplicationTests {

    @Autowired
    private MercadoPagoProps props;

    @Test
    void contextLoads() throws Exception {
        System.out.printf("props.getAccesstoken(): %s%n", props.getAccesstoken());
        System.out.printf("props.getNgrokurl(): %s%n", props.getNgrokurl());
    }

}
