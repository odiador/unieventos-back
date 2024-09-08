package co.edu.uniquindio.unieventos.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CouponMailSendDTO(

		@NotBlank @Email String email,
		@NotBlank @Length(min = 6, max = 6) String couponCode,
		@NotBlank String description

) {
}
