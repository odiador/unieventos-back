package co.edu.uniquindio.unieventos.dto.coupons;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFutureFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.NotNull;

public record CouponDTO(
		
		@Length(min = 1, max = 50)
		String code,

		@NotNull
		@Range(min = 0, max = 100) 
		Float discount,

		@NotNull
        @ValidDateTimeFutureFormat
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
