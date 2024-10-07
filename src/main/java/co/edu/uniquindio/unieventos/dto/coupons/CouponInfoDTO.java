package co.edu.uniquindio.unieventos.dto.coupons;

import java.time.LocalDateTime;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;

public record CouponInfoDTO(

		float discount,
		LocalDateTime expiryDate,
		String code,
		CouponStatus status,
		CouponType type,
		String name

) {}
