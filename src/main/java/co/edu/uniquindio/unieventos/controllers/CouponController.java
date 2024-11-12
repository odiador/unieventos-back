package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.coupons.AppliedCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponCodeDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CouponController {

	ResponseEntity<ResponseDTO<CouponCodeDTO>> createCoupon(@Valid CouponDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<Coupon>> findById(String id, HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<Coupon>> findByCode(String code, HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<AppliedCouponDTO>> applyByCode(@NotNull String code, HttpServletRequest request) throws Exception;

}
