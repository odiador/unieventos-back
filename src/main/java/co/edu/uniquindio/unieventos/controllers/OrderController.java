package co.edu.uniquindio.unieventos.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface OrderController {

	ResponseEntity<?> doPayment(@Valid DoPaymentDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> receiveMercadoPagoNotification(Map<String, Object> request) throws Exception;

	ResponseEntity<?> getPurchaseHistory(HttpServletRequest request) throws Exception;

	ResponseEntity<?> createOrder(@NotNull String id, String coupon, HttpServletRequest request) throws Exception;

}
