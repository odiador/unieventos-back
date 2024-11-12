package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.coupons.AppliedCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupons.GetCouponsDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CouponService {

	/**
	 * TODO
	 * 
	 * @param couponDTO
	 * @return the code of coupon
	 * @throws Exception
	 */
	String saveCoupon(@Valid CouponDTO couponDTO) throws Exception;

	List<CouponInfoDTO> getCouponsAdmin(@Valid GetCouponsDTO dto) throws Exception;
	
	Coupon getCouponByCode(String code) throws Exception;

	Coupon getCouponById(String id) throws Exception;

	AppliedCouponDTO applyCouponByCode(@NotNull String code) throws Exception;

	void changeCouponStatus(String code, CouponStatus cs) throws DocumentNotFoundException;

}
