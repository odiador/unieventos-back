package co.edu.uniquindio.unieventos.dto.carts;

import org.hibernate.validator.constraints.Length;

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
		@Length(min = 0, max = 100)
		String eventName,
		@NotBlank
		@Length(min = 0, max = 100)
		String localityName
) {}
