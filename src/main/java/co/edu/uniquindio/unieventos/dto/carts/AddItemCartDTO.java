package co.edu.uniquindio.unieventos.dto.carts;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddItemCartDTO(
		
		@ValidObjectId
		@NotBlank
		String cartId,

		@NotNull
		@Min(value = 1) 
		Integer quantity,

		@NotNull 
		@ValidObjectId
		String calendarId,

		@NotBlank
		@Length(min = 0, max = 100)
		String localityName,

		@NotBlank
		@Length(min = 0, max = 100)
		String eventName
) {}
