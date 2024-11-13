package co.edu.uniquindio.unieventos.dto.carts;

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
	    @ValidObjectId
	    String eventId,

	    @NotBlank
	    @ValidObjectId
	    String localityId

) {}
