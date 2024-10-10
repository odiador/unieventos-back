package co.edu.uniquindio.unieventos.dto.coupons;

import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import jakarta.validation.constraints.Min;

public record GetCouponsDTO(

		@Min(0) int page,
		@Min(0) int size,
		@ValidEnum(enumClass = CouponStatus.class, message = "Escribe un status válido")
		String status
) {}
