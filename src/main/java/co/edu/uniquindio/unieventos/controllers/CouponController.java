package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface CouponController {

	ResponseEntity<?> createCoupon(@Valid @NotNull CouponDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> findById(@Valid @NotBlank String id, HttpServletRequest request) throws Exception;

	ResponseEntity<?> findByCode(@Valid @NotBlank String code, HttpServletRequest request) throws Exception;

}
