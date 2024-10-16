package co.edu.uniquindio.unieventos.dto.orders;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderDTO(
		
		@NotBlank
		@ValidObjectId
		String cartId,

		@NotBlank
		@Email
		String email,
		
		@Length(min = 1, max = 50)
		String couponCode
		) {

}
