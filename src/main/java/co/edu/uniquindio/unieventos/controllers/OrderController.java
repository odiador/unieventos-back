package co.edu.uniquindio.unieventos.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import jakarta.validation.Valid;

public interface OrderController {

	ResponseEntity<?> doPayment(@Valid DoPaymentDTO dto) throws Exception;

	ResponseEntity<?> receiveMercadoPagoNotification(Map<String, Object> request) throws Exception;

}
