package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.orders.DoPaymentDTO;
import jakarta.validation.Valid;

public interface OrderController {

	ResponseEntity<?> doPayment(@Valid DoPaymentDTO dto) throws Exception;

}
