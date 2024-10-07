package co.edu.uniquindio.unieventos.dto.coupons;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.NotNull;

public record CouponDTO(
		
		String code,

		@NotNull
		@Range(min = 0, max = 100) 
		Float discount,

		@NotNull
        @ValidDateTimeFormat
		String expiryDate,

		@ValidEnum(enumClass = CouponStatus.class, message = "Ingresa un status valido")
		@NotNull
		String status,

		@ValidEnum(enumClass = CouponType.class, message = "Ingresa un tipo valido")
		@NotNull 
		String type, 

		@NotNull
		@Length(min = 0, max = 50) 
		String name

) {
}
