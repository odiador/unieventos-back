package co.edu.uniquindio.unieventos.dto.coupons;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record CouponDTO(
		
		@Range(min = 0,max = 100) float discount,
		@Validated @NotBlank LocalDateTime expiryDate,
		@Length(min = 6, max = 6) String code,
		@NotBlank CouponStatus status,
		@NotBlank CouponType type,
		@NotBlank @Max(50) String name
		
) {}
