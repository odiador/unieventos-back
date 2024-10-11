package co.edu.uniquindio.unieventos.dto.carts;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RemoveItemCartDTO(
		@NotNull
		@ValidObjectId
		String cartId,

		@ValidObjectId
		@NotNull
	    String calendarId,

	    @NotBlank
		@Length(min = 0, max = 100)
	    String eventName,

	    @NotBlank
		@Length(min = 0, max = 100)
	    String localityName

) {}
