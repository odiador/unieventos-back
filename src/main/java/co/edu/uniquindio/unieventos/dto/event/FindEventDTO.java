package co.edu.uniquindio.unieventos.dto.event;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FindEventDTO(
	
		@NotNull
		@ValidObjectId
		String idCalendar,
		@NotBlank
		@ValidObjectId
		String idEvent
		
) {}
