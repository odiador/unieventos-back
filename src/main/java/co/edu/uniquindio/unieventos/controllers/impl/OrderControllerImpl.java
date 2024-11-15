package co.edu.uniquindio.unieventos.controllers.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.OrderController;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.dto.orders.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

	@Autowired
	private AuthUtils authUtils;
	@Autowired
	private OrderService orderService;

	@Override
	@PostMapping("/pay")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<MercadoPagoURLDTO>> doPayment(@RequestBody @Valid DoPaymentDTO dto,
			HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		MercadoPagoURLDTO responseDTO = orderService.realizarPago(dto.id(), userId, request.getAttribute("origin").toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(
				"Proceso de pago iniciado con MercadoPago", responseDTO));
	}

	@Override
	@PostMapping("/pay/notification")
	public ResponseEntity<String> receiveMercadoPagoNotification(@RequestBody Map<String, Object> request) throws Exception {
		orderService.receiveMercadoPagoNotification(request);
		return ResponseEntity.ok("R");
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/getPurchaseHistory")
	public ResponseEntity<ResponseDTO<List<FindOrderDTO>>> getPurchaseHistory(HttpServletRequest request) throws Exception {
		authUtils.validateRoleMinClient(request);
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No se pudo encontrar el correo");
		return ResponseEntity.ok(new ResponseDTO<List<FindOrderDTO>>(
				"Historial encontrado",
				orderService.getPurchaseHistory(mail)));
	}
	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO<OrderDTO>> createOrder(@RequestParam("id") @NotNull String id,
			@RequestParam(name = "coupon", required = false) String coupon, HttpServletRequest request)
			throws Exception {
		authUtils.validateRoleMinClient(request);
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No se pudo encontrar el correo");
		return ResponseEntity.ok(new ResponseDTO<OrderDTO>("Orden creada con éxito",
				orderService.createOrder(new CreateOrderDTO(id, mail, coupon))));
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/find")
	public ResponseEntity<ResponseDTO<FindOrderDTO>> findOrder(@RequestParam("id") String id, HttpServletRequest request)
			throws Exception {
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No se pudo encontrar el correo");
		ResponseDTO<FindOrderDTO> responseDTO = new ResponseDTO<>("Orden obtenida con éxito", orderService.getOrderDTO(id, mail));
		return ResponseEntity.ok(responseDTO);
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/findAll")
	public ResponseEntity<ResponseDTO<List<FindOrderDTO>>> listOrders(HttpServletRequest request)
			throws Exception {
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No se pudo encontrar el correo");
		List<FindOrderDTO> orders = orderService.getOrdersDTO(mail);
		ResponseDTO<List<FindOrderDTO>> response = new ResponseDTO<>("Ordenes obtenidas con éxito", orders);
		return ResponseEntity.ok(response);
	}

}
