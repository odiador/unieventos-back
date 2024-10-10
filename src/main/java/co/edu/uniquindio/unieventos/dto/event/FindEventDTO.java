package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FindEventDTO(
	
		@NotNull
		@ValidObjectId
		String idCalendar,
		@NotBlank
		@Length(min = 0, max = 100)
		String name
		
) {}
