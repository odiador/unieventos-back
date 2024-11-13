package co.edu.uniquindio.unieventos.dto.carts;

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
		@ValidObjectId
		String localityId,

		@NotBlank
		@ValidObjectId
		String eventId
) {}
