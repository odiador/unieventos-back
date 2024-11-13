package co.edu.uniquindio.unieventos.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface OrderController {

	ResponseEntity<ResponseDTO<MercadoPagoURLDTO>> doPayment(@Valid DoPaymentDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<String> receiveMercadoPagoNotification(Map<String, Object> request) throws Exception;

	ResponseEntity<ResponseDTO<List<PurchaseDTO>>> getPurchaseHistory(HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<OrderDTO>> createOrder(@NotNull String id, String coupon, HttpServletRequest request)
			throws Exception;
	
	ResponseEntity<ResponseDTO<FindOrderDTO>> findOrder(String id, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<List<FindOrderDTO>>> listOrders(HttpServletRequest request) throws Exception;

}
