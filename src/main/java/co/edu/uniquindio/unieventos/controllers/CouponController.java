package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface CouponController {

	ResponseEntity<?> createCoupon(@Valid @RequestBody CouponDTO dto) throws Exception;

	ResponseEntity<?> findByCode(@Valid @NotBlank String code) throws Exception;

	ResponseEntity<?> findById(@Valid @NotBlank String id, HttpServletRequest request) throws Exception;
}
