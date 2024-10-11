package co.edu.uniquindio.unieventos.controllers.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.resources.preference.Preference;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.OrderController;
import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

	private final AuthUtils authUtils;
	@Autowired
	private OrderService orderService;

	@Override
	@PostMapping("/pay")
	public ResponseEntity<?> doPayment(@RequestBody @Valid DoPaymentDTO dto) throws Exception {
		Preference pref = orderService.realizarPago(dto.id());
		return ResponseEntity.status(HttpStatus.CREATED).body(new MercadoPagoURLDTO(pref.getInitPoint()));
	}

	@Override
	@PostMapping("/pay/notification")
	public ResponseEntity<?> receiveMercadoPagoNotification(@RequestBody Map<String, Object> request) throws Exception {
		orderService.receiveMercadoPagoNotification(request);
		return ResponseEntity.ok("R");
	}

	@Override
	@GetMapping("/getPurchaseHistory")
	public ResponseEntity<?> getPurchaseHistory(HttpServletRequest request) throws Exception {
		authUtils.validateRoleMinClient(request);
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No se pudo encontrar el correo");
		return ResponseEntity.ok(orderService.getPurchaseHistory(mail));
	}

}
