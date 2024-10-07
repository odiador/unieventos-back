package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import jakarta.validation.Valid;

public interface CouponController {

	ResponseEntity<?> createCoupon(@Valid @RequestBody CouponDTO dto) throws Exception;

	ResponseEntity<?> findById(@PathVariable String id) throws Exception;

	ResponseEntity<?> findByCode(@PathVariable String code) throws Exception;
}
