package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.resources.preference.Preference;

import co.edu.uniquindio.unieventos.controllers.OrderController;
import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.services.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderControllerImpl implements OrderController {

	@Autowired
	private OrderService orderService;

	@Override
	@PostMapping("/pay")
	public ResponseEntity<?> doPayment(@RequestBody @Valid DoPaymentDTO dto) throws Exception {
		Preference pref = orderService.realizarPago(dto.id());
		return ResponseEntity.status(HttpStatus.CREATED).body(new MercadoPagoURLDTO(pref.getInitPoint()));
	}

}
