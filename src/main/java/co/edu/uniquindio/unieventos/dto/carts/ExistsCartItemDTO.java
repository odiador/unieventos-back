package co.edu.uniquindio.unieventos.dto.carts;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;

public record ExistsCartItemDTO(
		@NotBlank
		@ValidObjectId
		String cartId,
		@NotBlank
		@ValidObjectId
		String calendarId,
		@NotBlank
		@ValidObjectId
		String eventId,
		@NotBlank
		@ValidObjectId
		String localityId
) {}
